package com.example.etchatapplication.repository.firestore

import android.net.Uri
import android.util.Log
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestorage.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageRepository: StorageRepository
) {

    private val userCollection = firestore.collection("users")
    private val groupCollection = firestore.collection("groups")
    private val chatRoomCollection = firestore.collection("chatrooms")

    fun addUser(uid: String, user: User, onResult: (Boolean) -> Unit) {
        userCollection.document(uid)
            .set(user)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResult(false)
            }
    }

    fun getUsersList(onResult: (List<User>) -> Unit) {
        userCollection.get()
            .addOnSuccessListener { result ->
                val users = result.map { it.toObject(User::class.java) }
                onResult(users)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResult(emptyList())
            }
    }

    suspend fun sendMessageWithImage(
        receiverEmail: String,
        message: String = "",
        imageUri: Uri,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val imageUrl = storageRepository.uploadImage(imageUri)
            sendMessageToRoom(receiverEmail, message, imageUrl, onResult)
        } catch (e: Exception) {
            onResult(false)
        }
    }

    fun getOrCreateChatRoom(receiverEmail: String, onComplete: (String?) -> Unit) {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email ?: return
        val chatRoomId = generateChatRoomId(listOf(senderEmail, receiverEmail))
        val chatRoomRef = chatRoomCollection.document(chatRoomId)

        chatRoomRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val newChatRoom = ChatRoom(
                    chatroomId = chatRoomId,
                    senderId = senderEmail,
                    receiverId = receiverEmail,
                    lastMessage = "",
                    participants = listOf(senderEmail, receiverEmail)
                )
                chatRoomRef.set(newChatRoom).addOnCompleteListener { task ->
                    onComplete(if (task.isSuccessful) chatRoomId else null)
                }
            } else {
                onComplete(chatRoomId)
            }
        }.addOnFailureListener { exception ->
            Log.d("FirebaseRepository", "Error fetching chat room ", exception)
            onComplete(null)
        }
    }

    private fun generateChatRoomId(participants: List<String>): String {
        return participants.sorted().joinToString(separator = "_")
    }

    fun sendMessageToRoom(
        chatRoomId: String,
        message: String,
        imageUrl: String,
        onResult: (Boolean) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email ?: return
        val messageId = UUID.randomUUID().toString()
        val chatMessage = Message(
            messageId = messageId,
            message = message,
            imageUrl = imageUrl,
            senderId = senderEmail,
            timestamp = System.currentTimeMillis(),
            isSent = true
        )

        val messageRef = chatRoomCollection.document(chatRoomId)
            .collection("messages")
            .document(messageId)

        firestore.runTransaction { transition ->
            val chatRoomRef = chatRoomCollection.document(chatRoomId)
            transition.set(messageRef, chatMessage)
            transition.update(chatRoomRef, "lastMessage", message)
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseRepository", "Message sent successfully")
            } else {
                Log.e("FirebaseRepository", "Failed to sent message", task.exception)
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
                Log.e("FirestoreRepository", "Error getting messages", error)
                return@addSnapshotListener
            }
            if (value != null) {
                val message = value.toObjects(Message::class.java)
                Log.d("FirebaseRepository", "Fetched MessageList :-${message.size}")
                trySend(message).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    fun getRoomChats(): Flow<List<ChatRoom>> = callbackFlow {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email
        val subscription =
            chatRoomCollection.whereArrayContains("participants", senderEmail ?: return@callbackFlow)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e("FirebaseRepository", "Error fetching room chats", error)
                        return@addSnapshotListener
                    }

                    if (value != null) {
                        val chatRooms = value.toObjects(ChatRoom::class.java)
                        Log.d("FirebaseRepo", "Chat room fetched :- ${chatRooms.size}")
                        chatRooms.forEach {
                            Log.d(
                                "FirestoreRepository",
                                "Chat Room ID: ${it.chatroomId}, Participants: ${it.senderId} ${it.receiverId}"
                            )
                        }
                        trySend(chatRooms).isSuccess
                    } else {
                        Log.d("FirestoreRepository", "No chat rooms found")
                    }
                }
        awaitClose { subscription.remove() }
    }

    //Create a group
    fun createGroup(name: String, selectedUsers: List<String>) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserEmail = currentUser?.email
        val members = selectedUsers + currentUserEmail
        val groupId = UUID.randomUUID().toString()
        val group = Group(
            id = groupId,
            name = name,
            users = members,
            createdBy = currentUserEmail.toString()
        )
        groupCollection.add(group)
            .addOnSuccessListener { documentListener ->
                Log.d("GROUP_ID", "createGroup: ${documentListener.id}")
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    // Get GroupList
    @Suppress("UNCHECKED_CAST")
    fun getGroups(callback: (List<Group>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Log.e("FirestoreRepo", "No current user found.")
            return
        }

        groupCollection
            .whereArrayContains("users", currentUser.email ?: return)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("FirestoreRepo", "Error fetching groups: ${e.message}", e)
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val groups = snapshots.documents.mapNotNull { doc ->
                        try {
                            Group(
                                id = doc.id,
                                name = doc.getString("name") ?: "",
                                users = doc.get("members") as? List<String> ?: emptyList()
                            )
                        } catch (ex: Exception) {
                            Log.e("FirestoreRepo", "Error parsing group document: ${doc.id}", ex)
                            null
                        }
                    }
                    callback(groups)
                } else {
                    Log.w("FirestoreRepo", "No groups found for the current user.")
                    callback(emptyList())
                }
            }
    }

    fun sendMessageToGroup(groupId: String, senderId: String, content: String) {
        val messageData = hashMapOf(
            "senderId" to senderId,
            "content" to content,
            "timestamp" to System.currentTimeMillis()
        )
        groupCollection.document(groupId)
            .collection("messages").add(messageData)
            .addOnSuccessListener { documentReference ->
                println("Message sent with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun getGroupMessages(groupId: String, callback: (List<Message>) -> Unit) {
        groupCollection.document(groupId)
            .collection("messages").orderBy("timestamp").addSnapshotListener { snapshots, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val messages = snapshots.documents.map { doc ->
                        Message(
                            senderId = doc.getString("senderId") ?: "",
                            message = doc.getString("content") ?: "",
                            timestamp = doc.getLong("timeStamp") ?: 0L
                        )
                    }
                    callback(messages)
                }
            }
    }

    fun getGroupDetails(groupId: String, onComplete: (Group?) -> Unit) {
        firestore.collection("groups").document(groupId).get().addOnSuccessListener { document ->
            val group = document.toObject(Group::class.java)
            onComplete(group)
        }
    }

    fun exitGroup(groupId: String, userId: String, onComplete: (Boolean) -> Unit) {
        val groupRef = firestore.collection("groups").document(groupId)
        groupRef.update("users", FieldValue.arrayRemove(userId))
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
