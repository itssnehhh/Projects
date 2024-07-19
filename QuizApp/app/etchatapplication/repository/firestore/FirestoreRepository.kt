package com.example.etchatapplication.repository.firestore

import com.example.etchatapplication.model.User
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    fun getAllUsers(onResult: (List<User>) -> Unit) {
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
        firestore.collection("users")
            .document(uid)
            .set(user)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }
}