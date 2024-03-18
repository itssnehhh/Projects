package com.example.platzifakestore.ApiService

import com.example.platzifakestore.Model.Category
import com.example.platzifakestore.Model.Product
import com.example.platzifakestore.Model.ProductItem
import com.example.platzifakestore.Model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("categories")
    fun getCategories():Call<Category>

    @GET("products/")
    fun getProductsByCategory(@Query("categoryId") categoryId: Int): Call<List<ProductItem>>

    @GET("users")
    fun getUsers():Call<User>
}
