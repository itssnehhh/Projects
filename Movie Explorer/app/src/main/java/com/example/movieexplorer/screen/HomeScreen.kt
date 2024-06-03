package com.example.movieexplorer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.movieexplorer.R
import com.example.movieexplorer.constants.Constants.DETAIL_SCREEN
import com.example.movieexplorer.model.Movies
import com.example.movieexplorer.model.data.homeMovieList

@Composable
fun HomeScreen(navController: NavHostController) {

    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var firstMovieList by remember { mutableStateOf<List<Movies>>(emptyList()) }
    var secondMovieList by remember { mutableStateOf<List<Movies>>(emptyList()) }
    var thirdMovieList by remember { mutableStateOf<List<Movies>>(emptyList()) }
    val titles = listOf("Popular", "Recent", "Favourite")

    LaunchedEffect(Unit) {
        isLoading = true
        homeMovieList(movieText = context.getString(R.string.ghost), context) { newMovies, error ->
            isLoading = false
            if (error != null) {
                errorMessage = error
            } else {
                firstMovieList = newMovies ?: emptyList()
            }
        }
        homeMovieList(movieText = context.getString(R.string.lucifer), context = context) { newMovies, error ->
            isLoading = false
            if (error != null) {
                errorMessage = error
            } else {
                secondMovieList = newMovies ?: emptyList()
            }
        }
        homeMovieList(movieText = context.getString(R.string.money), context = context) { newMovies, error ->
            isLoading = false
            if (error != null) {
                errorMessage = error
            } else {
                thirdMovieList = newMovies ?: emptyList()
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Red,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center)
            )
        } else {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        TitleDetail(title = titles[0])
                        HomeMovieCard(firstMovieList, navController)
                        TitleDetail(title = titles[1])
                        HomeMovieCard(secondMovieList, navController)
                        TitleDetail(title = titles[2])
                        HomeMovieCard(thirdMovieList, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun TitleDetail(title: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                text = title,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, bottom = 8.dp)
                    .weight(1f),
                maxLines = 2,
                style = MaterialTheme.typography.headlineSmall
            )
            TextButton(onClick = { }) {
                Text(
                    color = Color.Red,
                    text = stringResource(R.string.see_all),
                    modifier = Modifier.padding(end = 16.dp),
                    maxLines = 2,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun HomeMovieCard(movieList: List<Movies>, navController: NavHostController) {
    LazyRow {
        items(movieList) { movie ->
            Card(
                colors = CardDefaults.cardColors(Color.White),
                modifier = Modifier
                    .padding(4.dp)
                    .width(160.dp)
                    .height(220.dp)
                    .border(1.dp, Color.LightGray, CardDefaults.shape),
                onClick = {
                    navController.navigate("$DETAIL_SCREEN/${movie.imdbID}")
                }
            ) {
                val painter = rememberAsyncImagePainter(model = movie.poster)
                Image(
                    contentScale = ContentScale.FillBounds,
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color.Black)
                )
                Text(
                    color = Color.Red,
                    text = movie.title,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 2,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}