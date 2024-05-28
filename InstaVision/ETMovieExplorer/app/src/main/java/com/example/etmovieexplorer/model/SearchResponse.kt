package com.example.etmovieexplorer.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Search")
    val search: List<Movies>,
    @SerializedName("totalResults")
    val totalResults: String,
    @SerializedName("Response")
    val response: String
)
