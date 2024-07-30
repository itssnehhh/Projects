package com.example.chatapplication.model

data class Group(
    var id: String = "",
    val name: String = "",
    val users: List<String?> = emptyList(),
    val createdBy: String = ""
)
