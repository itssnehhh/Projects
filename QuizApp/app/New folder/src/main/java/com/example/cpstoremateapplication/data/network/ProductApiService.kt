package com.example.cpstoremateapplication.data.network

import com.example.cpstoremateapplication.data.model.Product
import retrofit2.http.GET

interface ProductApiService {

    @GET("products")
    suspend fun getProductList(): List<Product>

}