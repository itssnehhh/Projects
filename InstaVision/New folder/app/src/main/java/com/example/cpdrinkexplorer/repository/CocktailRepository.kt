package com.example.cpdrinkexplorer.repository

import com.example.drinkexploler.network.RetrofitInstance


class MocktailRepository {
    suspend fun searchMocktails(query: String) = RetrofitInstance.api.getSearchedDrink(query)
    suspend fun homeMocktails(query: String) = RetrofitInstance.api.getHomeDrink(query)
    suspend fun getMocktailDetails(id: String) = RetrofitInstance.api.getDrinkDetails(id)
}