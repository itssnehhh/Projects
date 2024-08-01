package com.example.etchatapplication.repository.firestore

import com.example.etchatapplication.model.User
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    firestore: FirebaseFirestore,
) {

    private val userCollection = firestore.collection("users")

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
}
