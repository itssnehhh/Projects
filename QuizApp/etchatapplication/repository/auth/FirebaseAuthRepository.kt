package com.example.etchatapplication.repository.auth

import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestore.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
) {

    fun getCurrentUser() = auth.currentUser

    suspend fun logIn(email: String, password: String): Boolean {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user != null
        } catch (e: Exception) {
            false
        }
    }

    suspend fun createAccount(
        fName: String,
        lName: String,
        email: String,
        password: String,
        imageUrl: String,
    ): Boolean {
        return try {
            val signUpResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = signUpResult.user
            user?.let {
                val userProfile = UserProfileChangeRequest.Builder()
                    .setDisplayName("$fName $lName")
                    .build()
                it.updateProfile(userProfile).await()
                val userDetails = User(
                    id = it.uid,
                    firstname = fName,
                    lastname = lName,
                    email = email,
                    image = imageUrl
                )
                userRepository.addUser(it.uid, userDetails)
            } != null
        } catch (e: Exception) {
            false
        }
    }


    fun logout() {
        auth.signOut()
    }
}