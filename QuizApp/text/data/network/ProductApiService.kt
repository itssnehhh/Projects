package com.example.cpstoremateapplication.data.network

import com.example.storemateproductapplication.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("products")
    suspend fun getProductList(): List<Product>

}