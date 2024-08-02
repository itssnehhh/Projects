package com.example.etchatapplication.model

data class Group(
    var id: String = "",
    val name: String = "",
    val users: List<String?> = emptyList(),
    val createdBy: String = "",
    val lastMessage :String = "",
    val timestamp:Long = 0L,
    val unreadCount: Int = 0
)
