package com.example.movieexplorer.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object{

        private const val BASE_URL = "http://www.omdbapi.com/"
        private var retrofit : Retrofit ?= null

        fun init(): ApiService {
            if (retrofit ==null){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiService::class.java)
        }
    }
}