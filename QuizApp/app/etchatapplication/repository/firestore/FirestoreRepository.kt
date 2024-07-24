package com.example.etchatapplication.repository.firestore

import android.net.Uri
import android.util.Log
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.fireStorage.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageRepository: StorageRepository
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

    suspend fun sendMessageWithImage(receiverEmail: String, message: String, imageUri: Uri, onResult: (Boolean) -> Unit) {
        try {
            val imageUrl = storageRepository.uploadImage(imageUri)
            sendMessage(receiverEmail, message, imageUrl, onResult)
        } catch (e: Exception) {
            // Handle upload error
            onResult(false)
        }
    }

    fun sendMessage(receiverEmail: String, message: String, imageUrl: String = "", onResult: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email ?: return

        val messageId = UUID.randomUUID().toString()
        val chatMessage = Message(
            id = messageId,
            senderId = senderEmail,
            receiverId = receiverEmail,
            message = message,
            imageUrl = imageUrl,
            isSent = true,
            timestamp = System.currentTimeMillis()
        )

        val senderDocRef = chatsCollection
            .document(senderEmail)
            .collection(receiverEmail)
            .document(messageId)

        val receiverDocRef = chatsCollection
            .document(receiverEmail)
            .collection(senderEmail)
            .document(messageId)

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


    //Create a group
    fun createGroup(name: String, selectedUsers: List<String>) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val members = selectedUsers + (currentUser?.email ?: "")
        val groupId = UUID.randomUUID().toString()
        val group = Group(id = groupId, name = name, users = members)
        groupCollection.document(groupId)
            .set(group)
            .addOnSuccessListener {
                Log.d("GROUP_ID", "createGroup: $groupId")
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }


    // Get GroupList
    fun getGroups(onResult: (List<Group>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Log.e("FirestoreRepo", "No current user found.")
            onResult(emptyList())
            return
        }

        groupCollection
            .whereArrayContains("users", currentUser.email ?: return)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("FirestoreRepo", "Error fetching groups: ${e.message}", e)
                    onResult(emptyList())
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val groups = snapshots.documents.mapNotNull { doc ->
                        try {
                            Group(
                                id = doc.id,
                                name = doc.getString("name") ?: "",
                                users = doc.get("users") as? List<String> ?: emptyList(),
                            )
                        } catch (ex: Exception) {
                            Log.e("FirestoreRepo", "Error parsing group document: ${doc.id}", ex)
                            null
                        }
                    }
                    onResult(groups)
                } else {
                    Log.w("FirestoreRepo", "No groups found for the current user.")
                    onResult(emptyList())
                }
            }
    }

    fun sendMessageToGroup(groupId: String, message: String, onResult: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email ?: return

        val messageId = UUID.randomUUID().toString()
        val groupMessage = Group(
            id = groupId,
            messages = listOf(
                Message(
                    id = messageId,
                    senderId = senderEmail,
                    receiverId = groupId,
                    message = message,
                    isSent = true,
                    timestamp = System.currentTimeMillis()
                )
            )
        )

        val groupDocRef = groupCollection
            .document(groupId)
            .collection("messages")
            .document(messageId)

        Log.d("FirestoreRepo", "Sending message to group: $groupId at path: ${groupDocRef.path}")

        firestore.runBatch { batch ->
            batch.set(groupDocRef, groupMessage)
        }.addOnCompleteListener { task ->
            onResult(task.isSuccessful)
        }
    }

    fun getGroupMessages(groupId: String): Flow<List<Message>> = callbackFlow {
        val documentRef = groupCollection.document(groupId)
            .collection("messages")
            .orderBy("timestamp")

        val subscription = documentRef.addSnapshotListener { value, _ ->
            if (value != null) {
                val messages = value.toObjects(Message::class.java)
                trySend(messages).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }
}
