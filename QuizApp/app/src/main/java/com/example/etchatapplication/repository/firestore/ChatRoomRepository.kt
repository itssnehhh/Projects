package com.example.etchatapplication.repository.firestore

import android.net.Uri
import android.util.Log
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.repository.storage.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRoomRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageRepository: StorageRepository,
) {
    private val chatRoomCollection = firestore.collection("chatrooms")

    suspend fun sendMessageWithImage(
        receiverId: String,
        message: String = "",
        imageUri: Uri,
        onResult: (Boolean) -> Unit,
    ) {
        try {
            val imageUrl = storageRepository.uploadImage(imageUri)
            sendMessageToRoom(receiverId, message, imageUrl, onResult)
        } catch (e: Exception) {
            onResult(false)
        }
    }

    fun getOrCreateChatRoom(receiverId: String, onComplete: (String?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderId = currentUser?.uid ?: return

        val participants = listOf(senderId, receiverId).sorted()

        chatRoomCollection.whereEqualTo("participants", participants)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    onComplete(result.documents.first().id)
                } else {
                    val newChatRoomRef = chatRoomCollection.document()
                    val newChatRoom = ChatRoom(
                        chatroomId = newChatRoomRef.id,
                        lastMessage = "",
                        participants = participants,
                        timestamp = System.currentTimeMillis()
                    )
                    newChatRoomRef.set(newChatRoom).addOnCompleteListener { task ->
                        onComplete(if (task.isSuccessful) newChatRoomRef.id else null)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ChatRepository", "Error checking existing chat room", exception)
                onComplete(null)
            }
    }

    fun sendMessageToRoom(
        chatRoomId: String,
        message: String,
        imageUrl: String?,
        onResult: (Boolean) -> Unit,
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderId = currentUser?.uid ?: return

        val messageId = chatRoomCollection.document(chatRoomId)
            .collection("messages")
            .document().id

        val time = System.currentTimeMillis()

        val chatMessage = Message(
            messageId = messageId,
            message = message,
            imageUrl = imageUrl ?: "",
            senderId = senderId,
            timestamp = time,
            isSent = true
        )

        val messageRef = chatRoomCollection.document(chatRoomId)
            .collection("messages")
            .document(messageId)

        firestore.runTransaction { transition ->
            val chatRoomRef = chatRoomCollection.document(chatRoomId)
            transition.set(messageRef, chatMessage)
            transition.update(chatRoomRef, "lastMessage", message)
            transition.update(chatRoomRef, "timestamp", time)
            transition.update(chatRoomRef,"unreadCount", FieldValue.increment(1))
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("ChatRepository", "Message sent successfully")
            } else {
                Log.e("ChatRepository", "Failed to send message", task.exception)
            }
            onResult(task.isSuccessful)
        }
    }

    fun getMessageFromChatRoom(chatRoomId: String): Flow<List<Message>> = callbackFlow {
        val messageRef = chatRoomCollection.document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp")

        val subscription = messageRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("ChatRepository", "Error getting messages", error)
                return@addSnapshotListener
            }
            if (value != null) {
                val message = value.toObjects(Message::class.java)
                Log.d("ChatRepository", "Fetched MessageList :-${message.size}")
                trySend(message).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    fun getRoomChats(): Flow<List<ChatRoom>> = callbackFlow {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderId = currentUser?.uid ?: return@callbackFlow
        val subscription = chatRoomCollection.whereArrayContains("participants", senderId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e("ChatRepository", "Error fetching room chats", error)
                        return@addSnapshotListener
                    }

                    if (value != null) {
                        val chatRooms = value.toObjects(ChatRoom::class.java)
                        Log.d("ChatRepository", "Chat room fetched :- ${chatRooms.size}")
                        chatRooms.forEach {
                            Log.d(
                                "ChatRepository",
                                "Chat Room ID: ${it.chatroomId}, Participants: ${it.participants}"
                            )
                        }
                        trySend(chatRooms).isSuccess
                    } else {
                        Log.d("ChatRepository", "No chat rooms found")
                    }
                }
        awaitClose { subscription.remove() }
    }

    fun markMessagesAsRead(chatRoomId: String, onComplete: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return

        val messagesRef = chatRoomCollection.document(chatRoomId).collection("messages")
        messagesRef.whereEqualTo("read", false).get()
            .addOnSuccessListener { result ->
                val unreadMessages = result.documents.filter { it.getString("senderId") != userId }

                firestore.runTransaction { transaction ->
                    for (message in unreadMessages) {
                        transaction.update(message.reference, "read", true)
                    }
                    transaction.update(chatRoomCollection.document(chatRoomId), "unreadCount", 0)
                }.addOnSuccessListener {
                    Log.d("ChatRepository", "Marked messages as read in $chatRoomId")
                    onComplete(true)
                }.addOnFailureListener { e ->
                    Log.e("ChatRepository", "Failed to mark messages as read: ${e.message}")
                    onComplete(false)
                }
            }
            .addOnFailureListener { e ->
                Log.e("ChatRepository", "Failed to fetch unread messages: ${e.message}")
                onComplete(false)
            }
    }

    fun getUnreadMessageCount(chatRoomId: String, callback: (Int) -> Unit) {
        val messagesRef = chatRoomCollection.document(chatRoomId).collection("messages")
        messagesRef.whereEqualTo("read", false).get()
            .addOnSuccessListener { result ->
                val unreadCount = result.documents.count { it.getString("senderId") != FirebaseAuth.getInstance().currentUser?.uid }
                Log.d("ChatRepository", "Unread messages in $chatRoomId: $unreadCount")
                callback(unreadCount)
            }
            .addOnFailureListener {
                Log.e("ChatRepository", "Failed to get unread messages count: ${it.message}")
                callback(0)
            }
    }
}