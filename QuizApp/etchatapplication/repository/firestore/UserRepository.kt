package com.example.etchatapplication.repository.firestore

import com.example.etchatapplication.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    firestore: FirebaseFirestore,
) {

    private val userCollection = firestore.collection("users")

    suspend fun addUser(uid: String, user: User): Boolean {
        return try {
            userCollection.document(uid).set(user).await()
            true
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    suspend fun getUsersList(): List<User> = withContext(Dispatchers.IO) {
        try {
            val result = userCollection.get().await()
            result.map { it.toObject(User::class.java) }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emptyList()
        }
    }


    suspend fun getUserDetails(userId: String): User = withContext(Dispatchers.IO) {
        try {
            val document = userCollection.document(userId).get().await()
            if (document != null && document.exists()) {
                val firstName = document.getString("firstname") ?: ""
                val lastName = document.getString("lastname") ?: ""
                val imageUrl = document.getString("image") ?: ""
                User(userId, firstName, lastName, imageUrl)
            } else {
                User(userId, "Unknown", "", "")
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            User(userId, "Unknown", "", "")
        }
    }

}
