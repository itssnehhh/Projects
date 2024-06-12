package com.example.etmovieexplorer.screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.constants.Constants
import com.example.etmovieexplorer.model.Movies
import com.example.etmovieexplorer.viewModel.SearchViewModel

@Composable
fun SearchScreen(navController: NavHostController, viewModel: SearchViewModel) {

    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val isLoadingState by viewModel.isLoading.collectAsState()
    val errorMessageState by viewModel.errorMessage.collectAsState()
    val searchedMovieListState by viewModel.searchedMovieList.collectAsState()


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
                    if (newText.isNotEmpty()) {
                        viewModel.searchMovies(newText, context)
                    }
                },
                label = { Text(text = stringResource(R.string.search)) },
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

        if (isLoadingState) {
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
                if (errorMessageState.isNotEmpty()) {
                    item {
                        Text(
                            text = errorMessageState,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(searchedMovieListState) { movie ->
                        MovieItem(movie = movie, navController)
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
                painter = painter,
                contentScale = ContentScale.Crop,
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