package com.example.etchatapplication.repository.fireStorage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor() {

    private val storageReference = FirebaseStorage.getInstance().reference

    suspend fun uploadImage(uri: Uri): String {
        val imageRef = storageReference.child("chat_images/${UUID.randomUUID()}.jpg")

        val uploadTask = imageRef.putFile(uri).await()
        return imageRef.downloadUrl.await().toString()
    }
}