package com.example.chatapplication.repository.firestore

import android.net.Uri
import android.util.Log
import com.example.chatapplication.model.ChatRoom
import com.example.chatapplication.model.Group
import com.example.chatapplication.model.Message
import com.example.chatapplication.model.User
import com.example.chatapplication.repository.storage.StorageRepository
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

        // Query to find existing chat rooms where the current user is a participant
        chatRoomCollection
            .whereArrayContains("participants", senderId)
            .get()
            .addOnSuccessListener { result ->
                var existingChatRoomId: String? = null
                for (document in result) {
                    val participants = document.get("participants") as? List<String> ?: continue
                    if (participants.contains(receiverId)) {
                        existingChatRoomId = document.id
                        break
                    }
                }

                if (existingChatRoomId != null) {
                    // Existing chat room with both participants
                    onComplete(existingChatRoomId)
                } else {
                    // No existing chat room, create a new one
                    val newChatRoomRef = chatRoomCollection.document() // Generate new document reference
                    val newChatRoom = ChatRoom(
                        chatroomId = newChatRoomRef.id, // Use the generated UID
                        lastMessage = "",
                        participants = listOf(senderId, receiverId)
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

    fun markMessageAsRead(chatRoomId: String, messageId: String, userId: String) {
        val messageRef = chatRoomCollection.document(chatRoomId).collection("messages").document(messageId)
        messageRef.update("readBy", FieldValue.arrayUnion(userId))
    }

    fun getUnreadMessageCount(chatRoomId: String, userId: String, callback: (Int) -> Unit) {
        chatRoomCollection.document(chatRoomId).collection("messages")
            .whereArrayContains("readBy", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val unreadCount = querySnapshot.size()
                callback(unreadCount)
            }
            .addOnFailureListener {
                callback(0)
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

        // Generate a unique ID for the message
        val messageId = chatRoomCollection.document(chatRoomId)
            .collection("messages")
            .document().id

        val chatMessage = Message(
            messageId = messageId,
            message = message,
            imageUrl = imageUrl ?: "",
            senderId = senderId,
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
        val senderId = currentUser?.uid
        val subscription =
            chatRoomCollection.whereArrayContains(
                "participants",
                senderId ?: return@callbackFlow
            )
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

    //Group Create
    fun createGroup(name: String, selectedUsers: List<String>) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid
        val members = selectedUsers + currentUserId
        val group = Group(
            id = "",  // We'll set the ID after creating the document reference
            name = name,
            users = members,
            createdBy = currentUserId.toString()
        )

        val groupDocRef =
            groupCollection.document()  // Create a document reference without writing the data
        group.id = groupDocRef.id  // Set the document ID in the group object

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

        groupCollection
            .whereArrayContains("users", currentUser.uid)
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
        groupCollection.document(groupId)
            .collection("messages").add(messageData)
            .addOnSuccessListener { documentReference ->
                println("Message sent with ID: ${documentReference.id}")
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