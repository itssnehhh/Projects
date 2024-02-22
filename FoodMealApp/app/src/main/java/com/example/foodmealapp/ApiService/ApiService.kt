package com.example.foodmealapp.ApiService

import com.example.foodmealapp.Model.CategoryData
import com.example.foodmealapp.Model.MealData
import com.example.foodmealapp.Model.MealIntroduction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("categories.php")
    fun getCategories():Call<CategoryData>

    @GET("filter.php")
    fun getMealByCategory(@Query("c") category:String):Call<MealData>

    @GET("lookup.php")
    fun getMealDetail(@Query("i") mealId : String):Call<MealIntroduction>
}