package com.example.etchatapplication.repository.firestore

import android.net.Uri
import android.util.Log
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val groupCollection = firestore.collection("groups")

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
            createdBy = currentUserId.toString(),
            timestamp = 0L,
            unreadCount = 0
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
        val currentUserId = currentUser?.uid ?: return

        groupCollection.whereArrayContains("users", currentUserId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("GroupRepository", "Error fetching groups: ${e.message}", e)
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val groups = snapshots.documents.mapNotNull { doc ->
                        try {
                            Group(
                                id = doc.id,
                                name = doc.getString("name") ?: "",
                                users = doc.get("members") as? List<String> ?: emptyList(),
                                lastMessage = doc.getString("lastMessage") ?: "",
                                timestamp = doc.getLong("timestamp") ?: 0L,
                                unreadCount = doc.get("unreadCount") as? Int? ?: 0
                            )
                        } catch (ex: Exception) {
                            Log.e("GroupRepository", "Error parsing group document: ${doc.id}", ex)
                            null
                        }
                    }
                    callback(groups)
                } else {
                    Log.w("GroupRepository", "No groups found for the current user.")
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
        groupRef.collection("messages").add(messageData)
            .addOnSuccessListener { documentReference ->
                println("Message sent with ID: ${documentReference.id}")
                groupRef.update("lastMessage", content)
                groupRef.update("timestamp", time)
                groupRef.update("unreadCount", count)
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
                            imageUrl = doc.getString("imageUrl") ?: "",
                            isRead = doc.getBoolean("read") ?: false
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
                groupRef.collection("messages").document().delete()
            } else {
                groupRef.update("users", FieldValue.arrayRemove(userId))
                    .addOnSuccessListener { onComplete(true) }
                    .addOnFailureListener { onComplete(false) }
            }
        }
    }

    fun markMessagesAsRead(groupId: String, onComplete: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return

        val messagesRef = groupCollection.document(groupId).collection("messages")
        messagesRef.whereEqualTo("read", false).get()
            .addOnSuccessListener { result ->
                val unreadMessages = result.documents.filter { it.getString("senderId") != userId }

                firestore.runTransaction { transaction ->
                    for (message in unreadMessages) {
                        transaction.update(message.reference, "read", true)
                    }
                    transaction.update(groupCollection.document(groupId), "unreadCount", 0)
                }.addOnSuccessListener {
                    Log.d("GroupRepository", "Marked messages as read in $groupId")
                    onComplete(true)
                }.addOnFailureListener { e ->
                    Log.e("GroupRepository", "Failed to mark messages as read: ${e.message}")
                    onComplete(false)
                }
            }
            .addOnFailureListener { e ->
                Log.e("GroupRepository", "Failed to fetch unread messages: ${e.message}")
                onComplete(false)
            }
    }

    fun getUnreadMessageCount(groupId: String, callback: (Int) -> Unit) {
        val messagesRef = groupCollection.document(groupId).collection("messages")
        messagesRef.whereEqualTo("read", false).get()
            .addOnSuccessListener { result ->
                val unreadCount =
                    result.documents.count { it.getString("senderId") != FirebaseAuth.getInstance().currentUser?.uid }
                Log.d("GroupRepository", "Unread messages in $groupId: $unreadCount")
                callback(unreadCount)
            }
            .addOnFailureListener {
                Log.e("GroupRepository", "Failed to get unread messages count: ${it.message}")
                callback(0)
            }
    }
}