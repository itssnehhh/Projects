package com.example.contactkeeper.data.model

data class Contact(
    val id: String = "",
    val name: String = "",
    val phoneNumber: List<String> = listOf(),
    val email: String = "",
    var profilePicture: String = "",
    val bloodGroup: String = "",
    val address: String = ""
)
