package com.example.etmovieexplorer.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etmovieexplorer.model.MovieDetail
import com.example.etmovieexplorer.model.data.getMovieDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {
    private val _movieDetails = MutableStateFlow<MovieDetail?>(null)
    val movieDetails: StateFlow<MovieDetail?> = _movieDetails

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadMovieDetail(imdbId: String, context: Context) {
        viewModelScope.launch {
            getMovieDetail(imdbId, { details ->
                _movieDetails.value = details
                _isLoading.value = false
            }, { error ->
                _errorMessage.value = error
                _isLoading.value = false
            })
        }
    }
}