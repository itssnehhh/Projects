package com.example.movieexplorer.screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.movieexplorer.R
import com.example.movieexplorer.constants.Constants.IMAGE_SCREEN
import com.example.movieexplorer.model.MovieDetail
import com.example.movieexplorer.model.data.getMovieDetail
import com.example.movieexplorer.preferences.PrefManager
import com.example.movieexplorer.ui.theme.CustomYellow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun DetailScreen(imdbId: String, navController: NavHostController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            DetailCard(imdbId, navController)
        }
    }
}

@Composable
fun DetailCard(imdbId: String, navController: NavHostController) {

    var movieDetails by remember { mutableStateOf<MovieDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val prefManager = remember { PrefManager(context) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(imdbId) {
        coroutineScope.launch {
            getMovieDetail(imdbId, { details ->
                movieDetails = details
                isLoading = false
            }, { error ->
                Log.e(context.getString(R.string.tag), context.getString(R.string.error, error))
                Toast.makeText(
                    context,
                    context.getString(R.string.error, error),
                    Toast.LENGTH_SHORT
                ).show()
                isLoading = false
            })
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Red)
        }
    } else {
        movieDetails?.let { movie ->
            Column {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = movie.poster, placeholder = painterResource(
                                id = R.drawable.ic_launcher_background
                            )
                        ),
                        contentDescription = movie.title,
                        modifier = Modifier
                            .clickable {
                                val encodeUrl = URLEncoder.encode(
                                    movie.poster,
                                    StandardCharsets.UTF_8.toString()
                                )
                                navController.navigate("$IMAGE_SCREEN/$encodeUrl")
                            }
                            .fillMaxSize()
                            .height(250.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.like),
                            contentDescription = "",
                            modifier = Modifier
                                .offset(y = 220.dp)
                                .size(60.dp)
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    prefManager.addToFavorite(movie)
                                    Toast
                                        .makeText(
                                            context,
                                            context.getString(R.string.added_to_favorites),
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(horizontal = 8.dp)
                                .offset(y = 220.dp)
                                .clickable {
                                    val shareText = context.getString(
                                        R.string.check_out,
                                        movie.title,
                                        movie.plot
                                    )
                                    val intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                    }
                                    val shareIntent = Intent.createChooser(intent, null)
                                    context.startActivity(shareIntent)
                                }
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
                Text(
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        color = CustomYellow,
                        text = "${movie.runtime} , ",
                        modifier = Modifier.padding(4.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        color = CustomYellow,
                        text = movie.release,
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
                TextValue(text = movie.plot)
                TextValue(text = stringResource(R.string.genre, movie.genre))
                TextValue(text = stringResource(R.string.languages, movie.language))
                TextValue(text = stringResource(R.string.actor, movie.actor))
                TextValue(text = stringResource(R.string.writer, movie.writer))
                TextValue(text = stringResource(R.string.director, movie.director))
                TextValue(text = stringResource(R.string.country, movie.country))
                TextValue(text = stringResource(R.string.awards, movie.awards))

                movie.ratings.let { ratings ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        if (ratings.isNotEmpty()) {
                            ratings.forEach { rating ->
                                Column(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .weight(1f)
                                        .border(1.dp, Color.LightGray),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        style = MaterialTheme.typography.titleMedium,
                                        text = rating.source,
                                        textAlign = TextAlign.Center,
                                        minLines = 2,
                                        modifier = Modifier.padding(
                                            start = 4.dp,
                                            end = 4.dp,
                                            top = 4.dp
                                        )
                                    )
                                    Text(
                                        style = MaterialTheme.typography.titleMedium,
                                        text = rating.value,
                                        modifier = Modifier.padding(
                                            start = 4.dp,
                                            end = 4.dp,
                                            bottom = 4.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                TextValue(text = stringResource(R.string.imdb_rating, movie.imdbRating))
                TextValue(text = stringResource(R.string.imdb_id, movie.imdbId))
            }
        }
    }
}

@Composable
fun TextValue(text: String) {
    Text(
        fontWeight = FontWeight.SemiBold,
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
    )
}
