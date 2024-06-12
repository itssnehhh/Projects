package com.example.etmovieexplorer.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etmovieexplorer.model.Movies
import com.example.etmovieexplorer.model.data.performSearch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchedMovieList = MutableStateFlow<List<Movies>>(emptyList())
    val searchedMovieList: StateFlow<List<Movies>> = _searchedMovieList

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var searchJob: Job? = null

    fun searchMovies(query: String, context: Context) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isLoading.value = true
            delay(500) // debounce time
            performSearch(query, context) { movies, error ->
                _isLoading.value = false
                if (error != null) {
                    _errorMessage.value = error
                    _searchedMovieList.value = emptyList()
                } else {
                    _errorMessage.value = ""
                    _searchedMovieList.value = movies ?: emptyList()
                }
            }
        }
    }
}