package com.example.etmovieexplorer.network

import com.example.etmovieexplorer.constants.Constants.API_KEY
import com.example.etmovieexplorer.model.MovieDetail
import com.example.etmovieexplorer.model.SearchResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("/")
    fun getSearchedMoviesList(
        @Query("s") search: String,
        @Query("apikey") apiKey: String = API_KEY,
    ): Call<SearchResponse>

    @GET("/")
    fun homeMoviesList(
        @Query("s") search: String,
        @Query("apikey") apiKey: String = API_KEY,
    ): Call<SearchResponse>

    @GET("/")
    fun getMovieDetails(
        @Query("i") imdbID: String,
        @Query("apikey") apiKey: String = API_KEY,
    ): Call<MovieDetail>

    @GET
    fun downloadImage(@Url imageUrl: String): Call<ResponseBody>

}