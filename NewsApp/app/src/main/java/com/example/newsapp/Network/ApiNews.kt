package com.example.newsapp.Network

import com.example.newsapp.Interface.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiNews {

    companion object{

        private var retrofit : Retrofit ?=null

        fun init():ApiService{
            if (retrofit==null){
                retrofit = Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiService::class.java)
        }
    }
}