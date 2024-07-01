package com.example.cpcontactkeeper.data.model

data class Contact(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: List<String> = listOf(),
    val profileImage: String = "",
    val bloodGroup: String = "",
    val address: String = ""
)