package com.example.cpcontactkeeper.data.model

import com.google.firebase.firestore.DocumentId

data class Contact(
    val id: String = "",
    val name: String = "",
    val phoneNumber: List<String> = listOf(),
    val email: String = "",
    val profilePicture: String = "",
    val bloodGroup: String = "",
    val address: String = ""
)
