package com.example.etchatapplication.model

data class ChatRoom(
    val chatroomId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val lastMessage: String = "",
    val participants: List<String> = emptyList()
)
