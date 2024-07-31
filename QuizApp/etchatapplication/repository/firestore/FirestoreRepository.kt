package com.example.etchatapplication.repository.firestore

import android.net.Uri
import android.util.Log
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.storage.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageRepository: StorageRepository,
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

    fun getUserDetails(userId: String, callback: (User) -> Unit) {
        userCollection.document(userId).get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val firstName = document.getString("firstname") ?: ""
                val lastName = document.getString("lastname") ?: ""
                val imageUrl = document.getString("image") ?: ""
                val user = User(userId, firstName, lastName, imageUrl)
                callback(user)
            } else {
                callback(User(userId, "Unknown", "", ""))
            }
        }.addOnFailureListener {
            callback(User(userId, "Unknown", "", ""))
        }
    }

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

        // Ensure the participants list is sorted
        val participants = listOf(senderId, receiverId).sorted()

        chatRoomCollection
            .whereEqualTo("participants", participants)
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
                Log.d("FirebaseRepository", "Error checking existing chat room", exception)
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
            transition.update(chatRoomRef,"unreadCount",FieldValue.increment(1))
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseRepository", "Message sent successfully")
            } else {
                Log.e("FirebaseRepository", "Failed to send message", task.exception)
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
        val senderId = currentUser?.uid ?: return@callbackFlow
        val subscription =
            chatRoomCollection.whereArrayContains("participants", senderId)
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
                                "Chat Room ID: ${it.chatroomId}, Participants: ${it.participants}"
                            )
                        }
                        trySend(chatRooms).isSuccess
                    } else {
                        Log.d("FirestoreRepository", "No chat rooms found")
                    }
                }
        awaitClose { subscription.remove() }
    }

    fun markMessagesAsRead(chatRoomId: String, onComplete: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return

        val messagesRef = chatRoomCollection.document(chatRoomId).collection("messages")
        messagesRef.whereEqualTo("isRead", false).get()
            .addOnSuccessListener { result ->
                val unreadMessages = result.documents.filter { it.getString("senderId") != userId }

                firestore.runTransaction { transaction ->
                    for (message in unreadMessages) {
                        transaction.update(message.reference, "isRead", true)
                    }
                    transaction.update(chatRoomCollection.document(chatRoomId), "unreadCount", 0)
                }.addOnSuccessListener {
                    Log.d("FirestoreRepository", "Marked messages as read in $chatRoomId")
                    onComplete(true)
                }.addOnFailureListener { e ->
                    Log.e("FirestoreRepository", "Failed to mark messages as read: ${e.message}")
                    onComplete(false)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepository", "Failed to fetch unread messages: ${e.message}")
                onComplete(false)
            }
    }

    fun getUnreadMessageCount(chatRoomId: String, callback: (Int) -> Unit) {
        val messagesRef = chatRoomCollection.document(chatRoomId).collection("messages")
        messagesRef.whereEqualTo("isRead", false).get()
            .addOnSuccessListener { result ->
                val unreadCount = result.documents.count { it.getString("senderId") != FirebaseAuth.getInstance().currentUser?.uid }
                Log.d("FirestoreRepository", "Unread messages in $chatRoomId: $unreadCount")
                callback(unreadCount)
            }
            .addOnFailureListener {
                Log.e("FirestoreRepository", "Failed to get unread messages count: ${it.message}")
                callback(0)
            }
    }

    //Group Create
    fun createGroup(name: String, selectedUsers: List<String>) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid
        val members = selectedUsers + currentUserId
        val group = Group(
            id = "",
            name = name,
            users = members,
            lastMessage = "",
            createdBy = currentUserId.toString()
        )
        val groupDocRef = groupCollection.document()
        group.id = groupDocRef.id
        groupDocRef.set(group)
            .addOnSuccessListener {
                Log.d("GROUP_ID", "createGroup: ${group.id}")
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    // Get GroupList
    fun getGroups(callback: (List<Group>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Log.e("FirestoreRepo", "No current user found.")
            return
        }

        groupCollection.whereArrayContains("users", currentUser.uid)
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
                                users = doc.get("members") as? List<String> ?: emptyList(),
                                lastMessage = doc.getString("lastMessage") ?: ""
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

    fun sendMessageToGroup(
        groupId: String,
        senderId: String,
        content: String,
        imageUrl: String? = null,
    ) {
        val messageData = hashMapOf(
            "senderId" to senderId,
            "content" to content,
            "imageUrl" to (imageUrl ?: ""),
            "timestamp" to System.currentTimeMillis()
        )
        val groupRef = groupCollection.document(groupId)
        groupRef.collection("messages").add(messageData)
            .addOnSuccessListener { documentReference ->
                println("Message sent with ID: ${documentReference.id}")
                groupRef.update("lastMessage", content)
                    .addOnSuccessListener {
                        println("Group's last message updated")
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                    }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun uploadImage(uri: Uri, onSuccess: (String) -> Unit) {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/${uri.lastPathSegment}")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
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
                            timestamp = doc.getLong("timeStamp") ?: 0L,
                            imageUrl = doc.getString("imageUrl") ?: ""
                        )
                    }
                    callback(messages)
                }
            }
    }

    fun getGroupDetails(groupId: String, onComplete: (Group?) -> Unit) {
        groupCollection.document(groupId).get().addOnSuccessListener { document ->
            val group = document.toObject(Group::class.java)
            onComplete(group)
        }
    }

    fun exitGroup(groupId: String, onComplete: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        val groupRef = groupCollection.document(groupId)
        groupRef.get().addOnSuccessListener { document ->
            val group = document.toObject(Group::class.java)
            if (userId == group?.createdBy) {
                groupRef.delete().addOnSuccessListener {
                    onComplete(true)
                }
            } else {
                groupRef.update("users", FieldValue.arrayRemove(userId))
                    .addOnSuccessListener { onComplete(true) }
                    .addOnFailureListener { onComplete(false) }
            }
        }
    }
}
