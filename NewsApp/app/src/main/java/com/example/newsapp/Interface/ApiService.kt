package com.example.newsapp.Interface

import com.example.newsapp.Model.DataNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines?apiKey=17f1e2868f064774b42744ba8db5d100")
    fun getNews(
        @Query("country") countryCode: String
    ): Call<DataNewsResponse>

    @GET("top-headlines?apiKey=17f1e2868f064774b42744ba8db5d100")
    fun getCategoryNews(
        @Query("country") countryCode: String,
        @Query("category") category: String,
    ): Call<DataNewsResponse>



}