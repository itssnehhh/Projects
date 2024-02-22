package com.example.musicmania.Deezer.Network

import com.example.musicmania.Deezer.Interface.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ApiClient {

    companion object {
        private var retrofit: Retrofit? = null

        fun init(): ApiService {
            if (retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiService::class.java)
        }
    }
}