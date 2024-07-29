package com.example.etchatapplication.model

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val message: String = "",
    val imageUrl: String? = "",
    val timestamp: Long = 0L,
    val isSent: Boolean = false,
    val status: String = "sent" // new field for message status
)
