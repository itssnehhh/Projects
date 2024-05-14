package com.example.cpmathquestquizapp.model

data class QuizCategory(
    val id: Int,
    val image: Int,
    val title: String,
    val topic: String = "",
    val totalQuiz: String = ""
)
