package com.example.cpimagedownloadapplication.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    fun downloadImage(@Url imageUrl:String): Call<ResponseBody>
}