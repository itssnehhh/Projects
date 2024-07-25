package com.example.etchatapplication.model

data class ChatRoom(
    val chatRoomId: String = "",
    val lastMessage: String = "",
    val participants: Map<String, Boolean> = emptyMap() // or List<String> if using an array
)
