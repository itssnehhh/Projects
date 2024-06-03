package com.example.movieexplorer.screen

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.movieexplorer.R
import com.example.movieexplorer.constants.Constants
import com.example.movieexplorer.model.Movies
import com.example.movieexplorer.model.data.performSearch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavHostController) {

    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }
    var searchedMovieList by remember { mutableStateOf<List<Movies>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            OutlinedTextField(
                maxLines = 1,
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    searchJob?.cancel()
                    searchJob = coroutineScope.launch {
                        delay(300) // debounce time
                        if (newText.isNotEmpty()) {
                            isLoading = true
                            performSearch(newText, context) { movies, error ->
                                isLoading = false
                                if (error != null) {
                                    errorMessage = error
                                } else {
                                    searchedMovieList = movies ?: emptyList()
                                }
                            }
                        } else {
                            searchedMovieList = emptyList()
                        }
                    }
                },
                label = { Text(text = context.getString(R.string.search)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp),
                tint = Color.Gray
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Red,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.CenterHorizontally)
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
                } else if (searchedMovieList.isNotEmpty()) {
                    items(searchedMovieList) { movie ->
                        MovieItem(movie = movie, navController)
                    }
                } else if(searchText.isNotEmpty()) {
                    item {
                        Text(
                            text = "No data available",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("${Constants.DETAIL_SCREEN}/${movie.imdbID}") }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, Color.LightGray, CardDefaults.shape)
                .fillMaxWidth()
        ) {
            val painter = rememberAsyncImagePainter(model = movie.poster)
            Image(
                contentScale = ContentScale.Crop,
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 12.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    maxLines = 2,
                    color = Color.Red,
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    fontWeight = FontWeight.SemiBold,
                    text = "Year: ${movie.year}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    fontWeight = FontWeight.SemiBold,
                    text = "Type: ${movie.type}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}