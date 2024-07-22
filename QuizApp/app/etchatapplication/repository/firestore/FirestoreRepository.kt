package com.example.etchatapplication.repository.firestore

import com.example.etchatapplication.model.ChatMessage
import com.example.etchatapplication.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
)
{

    private val userCollection = firestore.collection("users")
    private val chatsCollection = firestore.collection("chats")

    fun getUsersList(onResult: (List<User>) -> Unit) {
        firestore.collection("users")
            .get()
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
        userCollection
            .document(uid)
            .set(user)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }

    fun getMessages(senderEmail: String, receiverEmail: String): Flow<List<ChatMessage>> =
        callbackFlow {
            val docRef = chatsCollection
                .document(senderEmail)
                .collection(receiverEmail)
                .orderBy("timestamp")

            val subscription = docRef.addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val messages = snapshot.toObjects(ChatMessage::class.java)
                    trySend(messages).isSuccess
                }
            }

            awaitClose { subscription.remove() }
        }

    fun sendMessage(receiverEmail: String, message: String, onResult: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email ?: return

        val messageId = UUID.randomUUID().toString()
        val chatMessage = ChatMessage(
            id = messageId,
            senderId = senderEmail,
            receiverId = receiverEmail,
            message = message,
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
}