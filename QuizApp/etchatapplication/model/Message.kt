package com.example.etchatapplication.model


data class Message(
    val messageId: String="",
    val createdOn: Long=0L,
    val seen: Boolean=false,
    val sender: String="",
    val text: String="",
    val imageUrl: String = ""
)