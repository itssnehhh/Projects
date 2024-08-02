package com.example.etchatapplication.model

data class ChatRoom(
    val chatroomId: String = "",
    val lastMessage: String = "",
    val participants: List<String> = emptyList(),
    val timestamp: Long = 0L,
    val unreadCount: Int = 0
)
