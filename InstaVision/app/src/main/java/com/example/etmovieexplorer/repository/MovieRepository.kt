package com.example.etmovieexplorer.repository

import com.example.etmovieexplorer.model.MovieDetail
import com.example.etmovieexplorer.model.SearchResponse
import com.example.etmovieexplorer.network.ApiService
import okhttp3.ResponseBody
import retrofit2.Call

class MovieRepository(private val apiService: ApiService) {

    fun getSearchedMoviesList(search: String): Call<SearchResponse> {
        return apiService.getSearchedMoviesList(search)
    }

    fun homeMoviesList(search: String): Call<SearchResponse> {
        return apiService.homeMoviesList(search)
    }

    fun getMovieDetails(imdbID: String): Call<MovieDetail> {
        return apiService.getMovieDetails(imdbID)
    }

    fun downloadImage(imageUrl: String): Call<ResponseBody> {
        return apiService.downloadImage(imageUrl)
    }
}