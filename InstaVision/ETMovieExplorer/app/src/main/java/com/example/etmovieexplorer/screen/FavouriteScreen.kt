package com.example.etmovieexplorer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.model.FavoriteMovie
import com.example.etmovieexplorer.preferences.PrefManager

@Composable
fun FavouriteScreen(navController: NavHostController) {
    val context = LocalContext.current
    val prefManager = remember { PrefManager(context) }
    var favoriteMovies by remember { mutableStateOf(prefManager.getFavoriteMovies(context)) }

    LazyColumn {
        items(favoriteMovies) { movie ->
            FavouriteMovieItem(
                movie = movie,
                onRemoveClicked = { imdbID ->
                    prefManager.removeFromFavorites(context, imdbID)
                    favoriteMovies = prefManager.getFavoriteMovies(context) // Update the list
                },
                onMovieClicked = {
                    navController.navigate("movieDetails/${movie.imdbID}")
                }
            )
        }
    }
}

@Composable
fun FavouriteMovieItem(
    movie: FavoriteMovie,
    onRemoveClicked: (String) -> Unit,
    onMovieClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onMovieClicked() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = movie.posterUrl)
                        .apply {
                            crossfade(true)
                            placeholder(R.drawable.bg_image)
                            error(R.drawable.ic_launcher_background)
                        }.build()
                ),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f) // Make the text take the available space
            )

            IconButton(
                onClick = { onRemoveClicked(movie.imdbID) },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}