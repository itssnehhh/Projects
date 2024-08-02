package com.example.etchatapplication.repository.firestore

import android.net.Uri
import android.util.Log
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.repository.storage.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val imageUrl = storageRepository.uploadImage(imageUri)
            sendMessageToRoom(receiverId, message, imageUrl)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getOrCreateChatRoom(receiverId: String): String? = withContext(Dispatchers.IO) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderId = currentUser?.uid ?: return@withContext null

        val participants = listOf(senderId, receiverId).sorted()

        try {
            val result = chatRoomCollection.whereEqualTo("participants", participants).get().await()
            if (result.isEmpty) {
                val newChatRoomRef = chatRoomCollection.document()
                val newChatRoom = ChatRoom(
                    chatroomId = newChatRoomRef.id,
                    lastMessage = "",
                    participants = participants,
                    timestamp = System.currentTimeMillis()
                )
                newChatRoomRef.set(newChatRoom).await()
                newChatRoomRef.id
            } else {
                result.documents.first().id
            }
        } catch (e: Exception) {
            Log.d("ChatRepository", "Error checking existing chat room", e)
            null
        }
    }

    suspend fun sendMessageToRoom(
        chatRoomId: String,
        message: String,
        imageUrl: String?,
    ): Boolean = withContext(Dispatchers.IO) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderId = currentUser?.uid ?: return@withContext false

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

        return@withContext try {
            firestore.runTransaction { transition ->
                val chatRoomRef = chatRoomCollection.document(chatRoomId)
                transition.set(messageRef, chatMessage)
                transition.update(chatRoomRef, "lastMessage", message)
                transition.update(chatRoomRef, "timestamp", time)
                transition.update(chatRoomRef, "unreadCount", FieldValue.increment(1))
            }.await()
            Log.d("ChatRepository", "Message sent successfully")
            true
        } catch (e: Exception) {
            Log.e("ChatRepository", "Failed to send message", e)
            false
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

    suspend fun deleteChatRooms(chatRoomId: String): Boolean = withContext(Dispatchers.IO) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return@withContext false

        val chatRoomRef = chatRoomCollection.document(chatRoomId)
        return@withContext try {
            chatRoomCollection.whereArrayContains("participants", userId)
                .get()
                .await()
            chatRoomRef.delete().await()
            chatRoomRef.collection("messages").document().delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun markMessagesAsRead(chatRoomId: String): Boolean = withContext(Dispatchers.IO) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return@withContext false

        val messagesRef = chatRoomCollection.document(chatRoomId).collection("messages")
        return@withContext try {
            val result = messagesRef.whereEqualTo("read", false).get().await()
            val unreadMessages = result.documents.filter { it.getString("senderId") != userId }

            firestore.runTransaction { transaction ->
                for (message in unreadMessages) {
                    transaction.update(message.reference, "read", true)
                }
                transaction.update(chatRoomCollection.document(chatRoomId), "unreadCount", 0)
            }.await()
            Log.d("ChatRepository", "Marked messages as read in $chatRoomId")
            true
        } catch (e: Exception) {
            Log.e("ChatRepository", "Failed to mark messages as read: ${e.message}")
            false
        }
    }

    suspend fun getUnreadMessageCount(chatRoomId: String): Int = withContext(Dispatchers.IO) {
        val messagesRef = chatRoomCollection.document(chatRoomId).collection("messages")
        return@withContext try {
            val result = messagesRef.whereEqualTo("read", false).get().await()
            val unreadCount = result.documents.count { it.getString("senderId") != FirebaseAuth.getInstance().currentUser?.uid }
            Log.d("ChatRepository", "Unread messages in $chatRoomId: $unreadCount")
            unreadCount
        } catch (e: Exception) {
            Log.e("ChatRepository", "Failed to get unread messages count: ${e.message}")
            0
        }
    }
}
