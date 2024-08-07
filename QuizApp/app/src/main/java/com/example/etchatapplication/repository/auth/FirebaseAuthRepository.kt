package com.example.etchatapplication.repository.auth

import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestore.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) {
    fun logIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { logIn ->
                onResult(logIn.isSuccessful)
            }
    }

    fun signUp(
        fName: String,
        lName: String,
        email: String,
        password: String,
        imageUrl: String,
        onResult: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { signUp ->
            if (signUp.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val userProfile = UserProfileChangeRequest.Builder()
                        .setDisplayName("$fName $lName")
                        .build()
                    it.updateProfile(userProfile).addOnCompleteListener { profileUpdate ->
                        if (profileUpdate.isSuccessful) {
                            val userDetails = User(
                                id = it.uid,
                                firstname = fName,
                                lastname = lName,
                                email = email,
                                image = imageUrl
                            )
                            userRepository.addUser(it.uid, userDetails) { firestoreUpdate ->
                                onResult(firestoreUpdate)
                            }
                        } else {
                            onResult(false)
                        }
                    }
                }
            } else {
                onResult(false)
            }
        }
    }

    fun getCurrentUser() = auth.currentUser

    fun logOut() {
        auth.signOut()
    }
}