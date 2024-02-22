package com.example.foodmealapp.Network

import com.example.foodmealapp.ApiService.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCategory {

    companion object{

        private var retrofit : Retrofit ?= null

        fun init():ApiService{
            if (retrofit==null){
                retrofit = Retrofit.Builder()
                    .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiService::class.java)
        }
    }
}