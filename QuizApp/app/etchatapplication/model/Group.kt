package com.example.etchatapplication.model

data class Group(
    val id: String = "",
    val name: String = "",
    val users: List<String?> = emptyList(),
    val messages: List<Message?> = emptyList()
)
