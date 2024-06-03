package com.example.movieexplorer.model.data

import android.content.Context
import android.util.Log
import com.example.movieexplorer.R
import com.example.movieexplorer.model.Movies
import com.example.movieexplorer.model.SearchResponse
import com.example.movieexplorer.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun homeMovieList(movieText: String, context: Context, onResult: (List<Movies>?, String?) -> Unit) {
    ApiClient.init().homeMoviesList(movieText).enqueue(object : Callback<SearchResponse> {
        override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d(context.getString(R.string.tag), "onResponse: ${it.search}")
                    onResult(it.search, null)
                } ?: run {
                    Log.e(context.getString(R.string.tag), "onResponse: response body is null")
                    onResult(null, "No results found")
                }
            } else {
                Log.e(context.getString(R.string.tag), "Error: ${response.message()}")
                onResult(null, "Error: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
            Log.e(context.getString(R.string.tag), "onFailure: ${t.message}")
            onResult(null, "Failure: ${t.message}")
        }
    })
}
