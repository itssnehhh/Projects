package com.example.etchatapplication.model

data class Group(
    val id: String,
    val name: String,
    val members: List<String>
)