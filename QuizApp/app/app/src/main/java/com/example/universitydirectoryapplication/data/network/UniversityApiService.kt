package com.example.universitydirectoryapplication.data.network

import com.example.universitydirectoryapplication.data.model.University
import retrofit2.http.GET
import retrofit2.http.Query

interface UniversityApiService {
    @GET("search")
    suspend fun getUniversityList(@Query("country") country: String): List<University>
}