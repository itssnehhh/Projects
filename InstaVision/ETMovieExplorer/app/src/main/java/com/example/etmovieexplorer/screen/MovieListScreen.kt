package com.example.etmovieexplorer.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.etmovieexplorer.model.Movies
import com.example.etmovieexplorer.model.SearchResponse
import com.example.etmovieexplorer.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var movieList by remember { mutableStateOf<List<Movies>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column {
        SearchBar(
            shape = CardDefaults.shape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, color = Color.DarkGray, CardDefaults.elevatedShape),
            query = searchText,
            onQueryChange = { searchText = it },
            onSearch = {
                active = false
                errorMessage = ""
                isLoading = true
                performSearch(context, searchText) { movies, error ->
                    isLoading = false
                    if (error != null) {
                        errorMessage = error
                    } else {
                        movieList = movies ?: emptyList()
                    }
                }
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(text = "Search") },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (searchText.isNotEmpty()) {
                                searchText = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        ) {}

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .wrapContentSize()
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (errorMessage.isNotEmpty()) {
                    item {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else if (movieList.isNotEmpty()) {
                    items(movieList) { movie ->
                        MovieItem(movie = movie, navController)
                    }
                } else if (searchText.isNotEmpty()) {
                    item {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "No movies found",
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movies, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("movieDetails/${movie.imdbID}")
            }
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            val painter = rememberAsyncImagePainter(model = movie.poster)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Year: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Type: ${movie.type}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

private fun performSearch(context: Context, searchText: String, onResult: (List<Movies>?, String?) -> Unit) {
    ApiClient.init().getSearchedMoviesList(searchText).enqueue(object : Callback<SearchResponse> {
        override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("TAG", "onResponse: ${it.search}")
                    onResult(it.search, null)
                } ?: run {
                    Log.e("TAG", "onResponse: response body is null")
                    onResult(null, "No results found")
                }
            } else {
                Log.e("TAG", "Error: ${response.message()}")
                onResult(null, "Error: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
            Log.e("TAG", "onFailure: ${t.message}")
            onResult(null, "Failure: ${t.message}")
        }
    })
}

@Preview
@Composable
fun MovieListPreview() {
    MovieListScreen(navController = NavHostController(LocalContext.current))
}