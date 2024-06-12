package com.example.etmovieexplorer.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.etmovieexplorer.constants.Constants.LOGIN_SCREEN
import com.example.etmovieexplorer.constants.Constants.ON_BOARD_SCREEN
import com.example.etmovieexplorer.model.FavoriteMovie
import com.example.etmovieexplorer.model.MovieDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefManager(context: Context) {

    companion object {
        private const val PREF_NAME = "com.example.etmovieexplorer"
        private const val KEY_ONBOARD = ON_BOARD_SCREEN
        private const val KEY_LOGIN = LOGIN_SCREEN
        private const val USER_EMAIL = "userEmail"
        private const val USER_PASSWORD = "userPassword"
        private const val FAVOURITE_MOVIE = "favouriteMovie"
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

    fun saveUserDetail(email: String, password: String) {
        preferences.value.edit().putString(USER_EMAIL, email).putString(USER_PASSWORD, password)
            .apply()
    }

    fun getUserDetails(): Pair<String, String>? {
        val email = preferences.value.getString(USER_EMAIL, null)
        val password = preferences.value.getString(USER_PASSWORD, null)
        return if (email != null && password != null) Pair(email, password) else null
    }

    fun checkAlreadyExist(email: String): Boolean {
        val registeredEmail = preferences.value.getString(USER_EMAIL, null)
        return registeredEmail == email
    }

    fun addToFavorite(movie: MovieDetail) {
        val gson = Gson()
        val favoriteMovies = getFavoriteMovies().toMutableList()
        favoriteMovies.add(
            FavoriteMovie(
                imdbID = movie.imdbId,
                title = movie.title,
                poster = movie.poster
            )
        )
        preferences.value.edit().putString(FAVOURITE_MOVIE, gson.toJson(favoriteMovies)).apply()
    }

    fun getFavoriteMovies(): List<FavoriteMovie> {
        val gson = Gson()
        val json = preferences.value.getString(FAVOURITE_MOVIE, null)
        val type = object : TypeToken<List<FavoriteMovie>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun removeFromFavorites(imdbID: String) {
        val gson = Gson()
        val favoriteMovies = getFavoriteMovies().toMutableList()
        val iterator = favoriteMovies.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().imdbID == imdbID) {
                iterator.remove()
                break
            }
        }
        preferences.value.edit().putString(FAVOURITE_MOVIE, gson.toJson(favoriteMovies)).apply()
    }
}