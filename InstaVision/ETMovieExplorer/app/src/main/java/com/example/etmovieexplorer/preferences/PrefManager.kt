package com.example.etmovieexplorer.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.etmovieexplorer.constants.Constants.Companion.FAVOURITE
import com.example.etmovieexplorer.model.FavoriteMovie
import com.example.etmovieexplorer.model.MovieDetail
import com.example.etmovieexplorer.screen.Constants.LOGIN
import com.example.etmovieexplorer.screen.Constants.ON_BOARD
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefManager(context: Context) {

    companion object {
        private const val PREF_NAME = "com.example.etmovieexplorer"
        private const val KEY_ONBOARD = ON_BOARD
        private const val KEY_LOGIN = LOGIN
        private const val KEY_FAVOURITE = FAVOURITE
    }

    private var preferences: Lazy<SharedPreferences> = lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setOnBoardingStatus(status: Boolean) {
        preferences.value.edit().putBoolean(KEY_ONBOARD, status).apply()
    }

    fun getOnBoardingStatus(): Boolean {
        return preferences.value.getBoolean(KEY_ONBOARD, false)
    }

    fun setLoginStatus(status: Boolean) {
        preferences.value.edit().putBoolean(KEY_LOGIN, status).apply()
    }

    fun getLoginStatus(): Boolean {
        return preferences.value.getBoolean(KEY_LOGIN, false)
    }

    fun saveUser(email: String, password: String) {
        preferences.value.edit().putString("UserEmail", email).putString("UserPassword", password)
            .apply()
    }

    fun getUserCredentials(): Pair<String, String>? {
        val email = preferences.value.getString("UserEmail", null)
        val password = preferences.value.getString("UserPassword", null)
        return if (email != null && password != null) Pair(email, password) else null
    }

    fun userExists(email: String): Boolean {
        val storedEmail = preferences.value.getString("UserEmail", null)
        return storedEmail == email
    }

    fun addToFavorites(context: Context, movie: MovieDetail) {
        val gson = Gson()
        val favoriteMovies = getFavoriteMovies(context).toMutableList()
        favoriteMovies.add(
            FavoriteMovie(
                imdbID = movie.imdbId ?: "",
                title = movie.title ?: "",
                posterUrl = movie.poster ?: ""
            )
        )
        preferences.value.edit().putString(KEY_FAVOURITE, gson.toJson(favoriteMovies)).apply()
    }

    fun getFavoriteMovies(context: Context): List<FavoriteMovie> {
        val gson = Gson()
        val json = preferences.value.getString(KEY_FAVOURITE, null)
        val type = object : TypeToken<List<FavoriteMovie>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun isFavorite(context: Context, imdbID: String): Boolean {
        val favoriteMovies = getFavoriteMovies(context)
        return favoriteMovies.any { it.imdbID == imdbID }
    }

    fun removeFromFavorites(context: Context, imdbID: String) {
        val gson = Gson()
        val favoriteMovies = getFavoriteMovies(context).toMutableList()
        val iterator = favoriteMovies.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().imdbID == imdbID) {
                iterator.remove()
                break
            }
        }
        preferences.value.edit().putString(KEY_FAVOURITE, gson.toJson(favoriteMovies)).apply()
    }
}