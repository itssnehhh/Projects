package com.example.etmovieexplorer.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.constants.Constants.DETAIL_SCREEN
import com.example.etmovieexplorer.model.FavoriteMovie
import com.example.etmovieexplorer.viewModel.FavouriteViewModel

@Composable
fun FavouriteScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel : FavouriteViewModel = remember {
        FavouriteViewModel(context)
    }

    val favoriteMovies by viewModel.favoriteMovies.collectAsState()

    LazyColumn {
        if (favoriteMovies.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.no_favourite_added),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(favoriteMovies) { favoriteMovie ->
                FavouriteMovieItem(
                    movie = favoriteMovie,
                    onRemovedClick = { imdbID ->
                        viewModel.removeFromFavorites(imdbID)
                        Toast.makeText(
                            context,
                            context.getString(R.string.removed_from_favourite_list), Toast.LENGTH_SHORT
                        ).show()
                    },
                    onMovieClicked = {
                        navController.navigate("$DETAIL_SCREEN/${favoriteMovie.imdbID}")
                    }
                )
            }
        }
    }
}

@Composable
fun FavouriteMovieItem(
    movie: FavoriteMovie,
    onRemovedClick: (String) -> Unit,
    onMovieClicked: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onMovieClicked() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.border(1.dp, Color.Black, CardDefaults.shape)) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .border(1.dp, Color.Black, CardDefaults.shape),
                painter = rememberAsyncImagePainter(model = movie.poster),
                contentDescription = movie.title
            )
            Text(
                style = MaterialTheme.typography.titleLarge,
                color = Color.Red,
                text = movie.title,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            IconButton(
                onClick = { onRemovedClick(movie.imdbID) },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "", tint = Color.Red)
            }
        }
    }
}