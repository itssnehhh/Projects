package com.example.chatapplication.model

data class ChatRoom(
    val chatroomId: String = "",
    val lastMessage: String = "",
    val participants: List<String> = emptyList(),
    val timeStamp : Long = 0L
)
