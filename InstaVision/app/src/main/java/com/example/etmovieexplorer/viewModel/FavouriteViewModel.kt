package com.example.etmovieexplorer.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etmovieexplorer.model.FavoriteMovie
import com.example.etmovieexplorer.preferences.PrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouriteViewModel(context: Context) : ViewModel() {

    private val prefManager = PrefManager(context)

    private val _favoriteMovies = MutableStateFlow<List<FavoriteMovie>>(emptyList())
    val favoriteMovies: StateFlow<List<FavoriteMovie>> = _favoriteMovies

    init {
        loadFavoriteMovies()
    }

    private fun loadFavoriteMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteMovies.emit(prefManager.getFavoriteMovies())
        }
    }

    fun removeFromFavorites(imdbID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            prefManager.removeFromFavorites(imdbID)
            loadFavoriteMovies()
        }
    }
}