package com.example.etmovieexplorer.model.data

import com.example.etmovieexplorer.model.MovieDetail
import com.example.etmovieexplorer.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getMovieDetail(
    imdbID: String,
    onSuccess: (MovieDetail) -> Unit,
    onError: (String) -> Unit
) {
    ApiClient.init().getMovieDetails(imdbID).enqueue(object : Callback<MovieDetail> {
        override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
            if (response.isSuccessful) {
                response.body()?.let { onSuccess(it) } ?: onError("No data available")
            } else {
                onError(response.message())
            }
        }

        override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
            onError(t.message ?: "Unknown error")
        }
    })
}