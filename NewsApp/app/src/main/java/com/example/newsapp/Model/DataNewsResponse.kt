package com.example.newsapp.Model

data class DataNewsResponse(
    var status: String,
    var totalResults: Int,
    var articles:MutableList<Article>
)
