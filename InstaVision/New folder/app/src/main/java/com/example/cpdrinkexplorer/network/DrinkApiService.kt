package com.example.drinkexploler.network

import com.example.cpdrinkexplorer.data.model.CocktailDetailResponse
import com.example.cpdrinkexplorer.data.model.CocktailResponse
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinkApiService {

    @GET("search.php")
    suspend fun getSearchedDrink(@Query("s") search: String): CocktailResponse

    @GET("search.php")
    suspend fun getHomeDrink(@Query("s") search: String): CocktailResponse

    @GET("lookup.php")
    suspend fun getDrinkDetails(@Query("i") search: String): CocktailDetailResponse

}