package com.example.etmovieexplorer.screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.constants.Constants.Companion.IMAGE_SCREEN
import com.example.etmovieexplorer.model.MovieDetail
import com.example.etmovieexplorer.network.ApiClient
import com.example.etmovieexplorer.preferences.PrefManager
import com.example.etmovieexplorer.ui.theme.CustomYellow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MovieDetailScreen(imdbID: String, navController: NavHostController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            DetailCard(imdbID, navController)
        }
    }
}


@Composable
fun DetailCard(imdbID: String, navController: NavHostController) {
    var movieDetails by remember { mutableStateOf<MovieDetail?>(null) }
    val context = LocalContext.current
    val prefManager = remember {
        PrefManager(context)
    }
    LaunchedEffect(imdbID) {
        ApiClient.init().getMovieDetails(imdbID).enqueue(object : Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                if (response.isSuccessful) {
                    movieDetails = response.body()
                } else {
                    Log.e("TAG", "Error: ${response.message()}")
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                Log.e("TAG", "Failure: ${t.message}")
                Toast.makeText(context, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    movieDetails?.let { movie ->
        Column {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberAsyncImagePainter(model = movie.poster),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            val encodedUrl =
                                URLEncoder.encode(movie.poster, StandardCharsets.UTF_8.toString())
                            navController.navigate("$IMAGE_SCREEN/$encodedUrl")
                        }
                        .fillMaxSize()
                        .height(200.dp),
                    contentScale = ContentScale.FillBounds
                )
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.like),
                        contentDescription = null,
                        modifier = Modifier
                            .offset(y = 170.dp)
                            .size(60.dp)
                            .padding(horizontal = 8.dp)
                            .clickable {
                                prefManager.addToFavorites(context, movie)
                                Toast
                                    .makeText(context, "Added to favorites", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(horizontal = 8.dp)
                            .offset(y = 170.dp)
                            .clickable {
                                val shareText = "Check out ${movie.title}! \n\n${movie.plot}"
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
            Spacer(modifier = Modifier.height(40.dp))
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
                    text = "${movie.runtime},",
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
            Spacer(modifier = Modifier.height(20.dp))
            TextValue(text = "Genre :- ${movie.genre}")
            TextValue(text = "Languages :- ${movie.language}")
            TextValue(text = "Actor :- ${movie.actor}")
            TextValue(text = "Writer :- ${movie.writer}")
            TextValue(text = "Director :- ${movie.director}")
            TextValue(text = "Country :- ${movie.country}")
            TextValue(text = "Awards :- ${movie.awards}")

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
            TextValue(text = "IMDB Rating :- ${movie.imdbRating}")
            TextValue(text = "IMDB Id :- ${movie.imdbId}")
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

@Preview
@Composable
fun MovieDetailScreenPreview() {
    MovieDetailScreen(imdbID = "tt1229238", navController = NavHostController(LocalContext.current))
}
