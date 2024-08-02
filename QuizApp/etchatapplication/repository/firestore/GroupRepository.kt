package com.example.etchatapplication.repository.firestore

import android.net.Uri
import android.util.Log
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val groupCollection = firestore.collection("groups")

    // Create Group
    suspend fun createGroup(name: String, selectedUsers: List<String>): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        val members = selectedUsers + currentUserId
        val group = Group(
            id = "",
            name = name,
            users = members,
            lastMessage = "",
            createdBy = currentUserId,
            timestamp = System.currentTimeMillis(),
            unreadCount = 0
        )
        val groupDocRef = groupCollection.document()
        group.id = groupDocRef.id
        return try {
            groupDocRef.set(group).await()  // Assuming you have `await` extension function
            group.id
        } catch (e: Exception) {
            Log.e("GroupRepository", "Failed to create group", e)
            throw e
        }
    }

    // Get Group List
    suspend fun getGroups(): List<Group> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid ?: throw IllegalStateException("User not authenticated")

        return try {
            val snapshots = groupCollection.whereArrayContains("users", currentUserId)
                .get().await()  // Assuming you have `await` extension function
            snapshots.documents.mapNotNull { doc ->
                try {
                    Group(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        users = doc.get("users") as? List<String> ?: emptyList(),
                        lastMessage = doc.getString("lastMessage") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L,
                        unreadCount = doc.getLong("unreadCount")?.toInt() ?: 0
                    )
                } catch (ex: Exception) {
                    Log.e("GroupRepository", "Error parsing group document: ${doc.id}", ex)
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("GroupRepository", "Error fetching groups", e)
            emptyList()
        }
    }

    // Send Message to Group
    suspend fun sendMessageToGroup(
        groupId: String,
        senderId: String,
        content: String,
        imageUrl: String? = null,
    ) {
        val time = System.currentTimeMillis()
        val count = FieldValue.increment(1)
        val messageData = hashMapOf(
            "senderId" to senderId,
            "content" to content,
            "imageUrl" to (imageUrl ?: ""),
            "timestamp" to time,
            "unreadCount" to count
        )
        val groupRef = groupCollection.document(groupId)

        try {
            val documentReference = groupRef.collection("messages").add(messageData).await()
            groupRef.update("lastMessage", content).await()
            groupRef.update("timestamp", time).await()
            groupRef.update("unreadCount", count).await()
            Log.d("GroupRepository", "Message sent with ID: ${documentReference.id}")
        } catch (e: Exception) {
            Log.e("GroupRepository", "Failed to send message", e)
            throw e
        }
    }

    // Upload Image
    suspend fun uploadImage(uri: Uri): String {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${uri.lastPathSegment}")
        return try {
            storageRef.putFile(uri).await()  // Assuming you have `await` extension function
            val downloadUri = storageRef.downloadUrl.await()
            downloadUri.toString()
        } catch (e: Exception) {
            Log.e("GroupRepository", "Failed to upload image", e)
            throw e
        }
    }

    suspend fun getGroupMessages(groupId: String): Flow<List<Message>> = callbackFlow {
        val messageRef = groupCollection.document(groupId).collection("messages")
            .orderBy("timestamp")

        val subscription = messageRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("GroupRepository", "Error fetching messages", error)
                return@addSnapshotListener
            }
            if (value != null) {
                val messages = value.toObjects(Message::class.java)
                trySend(messages).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    // Get Group Details
    suspend fun getGroupDetails(groupId: String): Group? {
        return try {
            val document = groupCollection.document(groupId).get().await()  // Assuming you have `await` extension function
            document.toObject(Group::class.java)
        } catch (e: Exception) {
            Log.e("GroupRepository", "Failed to get group details", e)
            null
        }
    }

    suspend fun exitGroup(groupId: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return false

        val groupRef = groupCollection.document(groupId)
        return try {
            val group = groupRef.get().await().toObject(Group::class.java)
            if (userId == group?.createdBy) {
                groupRef.delete().await()
                groupRef.collection("messages").document().delete().await()
                true
            } else {
                groupRef.update("users", FieldValue.arrayRemove(userId)).await()
                true
            }
        } catch (e: Exception) {
            Log.e("GroupRepository", "Failed to exit group", e)
            false
        }
    }

    // Mark Messages as Read
    suspend fun markMessagesAsRead(groupId: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return false

        val messagesRef = groupCollection.document(groupId).collection("messages")
        return try {
            val result = messagesRef.whereEqualTo("read", false).get().await()  // Assuming you have `await` extension function
            val unreadMessages = result.documents.filter { it.getString("senderId") != userId }

            firestore.runTransaction { transaction ->
                for (message in unreadMessages) {
                    transaction.update(message.reference, "read", true)
                }
                transaction.update(groupCollection.document(groupId), "unreadCount", 0)
            }.await()
            Log.d("GroupRepository", "Marked messages as read in $groupId")
            true
        } catch (e: Exception) {
            Log.e("GroupRepository", "Failed to mark messages as read", e)
            false
        }
    }

    // Get Unread Message Count
    suspend fun getUnreadMessageCount(groupId: String): Int {
        val messagesRef = groupCollection.document(groupId).collection("messages")
        return try {
            val result = messagesRef.whereEqualTo("read", false).get().await()  // Assuming you have `await` extension function
            result.documents.count { it.getString("senderId") != FirebaseAuth.getInstance().currentUser?.uid }
        } catch (e: Exception) {
            Log.e("GroupRepository", "Failed to get unread messages count", e)
            0
        }
    }
}
