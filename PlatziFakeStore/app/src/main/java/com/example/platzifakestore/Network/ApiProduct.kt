package com.example.platzifakestore.Network

import com.example.platzifakestore.ApiService.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiProduct {

    companion object{

        private var retrofit : Retrofit ?= null

        fun init():ApiService{
            if (retrofit==null){
                retrofit = Retrofit.Builder()
                    .baseUrl("https://api.escuelajs.co/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiService::class.java)
        }
    }
}