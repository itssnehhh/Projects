package com.example.etchatapplication.repository.firestore

import android.util.Log
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.model.User
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
) {

    private val userCollection = firestore.collection("users")
    private val chatsCollection = firestore.collection("chats")
    private val groupCollection = firestore.collection("groups")

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

    fun addUser(uid: String, user: User, onResult: (Boolean) -> Unit) {
        userCollection.document(uid)
            .set(user)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    fun sendMessage(receiverEmail: String, message: String, onResult: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email ?: return

        val messageId = UUID.randomUUID().toString()
        val chatMessage = Message(
            id = messageId,
            senderId = senderEmail,
            receiverId = receiverEmail,
            message = message,
            isSent = true,
            timestamp = System.currentTimeMillis()
        )

        // Reference to Firestore document for the sender
        val senderDocRef = chatsCollection
            .document(senderEmail)
            .collection(receiverEmail)
            .document(messageId)

        // Reference to Firestore document for the receiver
        val receiverDocRef = chatsCollection
            .document(receiverEmail)
            .collection(senderEmail)
            .document(messageId)

        // Run a batch write to ensure both documents are updated automatically.
        firestore.runBatch { batch ->
            batch.set(senderDocRef, chatMessage)
            batch.set(receiverDocRef, chatMessage)
        }.addOnCompleteListener { task ->
            onResult(task.isSuccessful)
        }
    }

    fun getMessages(senderEmail: String, receiverEmail: String): Flow<List<Message>> =
        callbackFlow {
            val documentRef = chatsCollection.document(senderEmail)
                .collection(receiverEmail)
                .orderBy("timestamp")

            // Added a snapshot listener to get real-time updates
            val subscription = documentRef.addSnapshotListener { value, _ ->
                if (value != null) {
                    val messages = value.toObjects(Message::class.java)
                    trySend(messages).isSuccess
                }
            }
            // Clean up the listener when the flow is closed
            awaitClose { subscription.remove() }
        }

    fun createGroup(name: String, selectedUsers: List<String>, callback: (String) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val members = selectedUsers + currentUser?.uid
        val groupData = hashMapOf(
            "name" to name,
            "members" to members
        )
        groupCollection.add(groupData)
            .addOnSuccessListener { documentReference ->
                callback(documentReference.id)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun sendMessageToGroup(groupId: String, senderId: String, content: String) {
        val messageData = hashMapOf(
            "senderId" to senderId,
            "content" to content,
            "timestamp" to FieldValue.serverTimestamp()
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
                            timestamp = doc.getTimestamp("timestamp").toString().toLong()
                        )
                    }
                    callback(messages)
                }
            }
    }

    // In FirestoreRepository
    fun getGroups(callback: (List<Group>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Log.e("FirestoreRepo", "No current user found.")
            return
        }

        groupCollection
            .whereArrayContains("members", currentUser.uid)
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
                                members = doc.get("members") as? List<String> ?: emptyList()
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
}