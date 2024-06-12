package com.example.etmovieexplorer.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.model.Movies
import com.example.etmovieexplorer.model.data.homeMovieList
import com.example.etmovieexplorer.network.ApiService
import com.example.etmovieexplorer.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _firstMovieList = MutableStateFlow<List<Movies>>(emptyList())
    val firstMovieList: StateFlow<List<Movies>> = _firstMovieList

    private val _secondMovieList = MutableStateFlow<List<Movies>>(emptyList())
    val secondMovieList: StateFlow<List<Movies>> = _secondMovieList

    private val _thirdMovieList = MutableStateFlow<List<Movies>>(emptyList())
    val thirdMovieList: StateFlow<List<Movies>> = _thirdMovieList

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadMovies(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            loadFirstMovieList(context)
            loadSecondMovieList(context)
            loadThirdMovieList(context)
            _isLoading.value = false
        }
    }

    private fun loadFirstMovieList(context: Context) {
        homeMovieList(movieText = context.getString(R.string.ghost), context) { newMovies, error ->
            if (error != null) {
                _errorMessage.value = error
            } else {
                _firstMovieList.value = newMovies ?: emptyList()
            }
        }
    }

    private fun loadSecondMovieList(context: Context) {
        homeMovieList(movieText = context.getString(R.string.lucifer), context) { newMovies, error ->
            if (error != null) {
                _errorMessage.value = error
            } else {
                _secondMovieList.value = newMovies ?: emptyList()
            }
        }
    }

    private fun loadThirdMovieList(context: Context) {
        homeMovieList(movieText = context.getString(R.string.money), context) { newMovies, error ->
            if (error != null) {
                _errorMessage.value = error
            } else {
                _thirdMovieList.value = newMovies ?: emptyList()
            }
        }
    }
}