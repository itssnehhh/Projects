package com.example.musicplayer.ui.component

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.musicplayer.R
import com.example.musicplayer.data.model.Song
import com.example.musicplayer.ui.screen.home.HomeViewModel
import java.io.IOException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(songs: List<Song>, currentSongIndex: Int, onSongChange: (Int) -> Unit) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val player = remember { MediaPlayer() }
    var sliderPosition by remember { mutableStateOf(0f) }
    var currentPosition by remember { mutableStateOf(0) }
    var duration by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }

    fun playSong(songIndex: Int) {
        try {
            player.reset()
            player.setDataSource(songs[songIndex].path)
            player.prepareAsync()
            player.setOnPreparedListener {
                it.start()
                duration = it.duration
                isPlaying = true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(songs[currentSongIndex].path) {
        playSong(currentSongIndex)
    }


    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = player.currentPosition
            sliderPosition = (currentPosition.toFloat() / duration * 100).coerceIn(0f..100f)
            kotlinx.coroutines.delay(1000) // Update every second
        }
    }


    ModalBottomSheet(
        onDismissRequest = {
            player.pause()
            player.release() // Release resources
        },
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = songs[currentSongIndex].image),
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .aspectRatio(1f)
            )
            Text(
                text = songs[currentSongIndex].name,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 4.dp)
                    .fillMaxWidth()
            )
            Text(
                text = songs[currentSongIndex].singerName,
                maxLines = 1,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )

            Slider(
                value = sliderPosition,
                onValueChange = { value ->
                    sliderPosition = value
                    player.seekTo((value / 100 * duration).toInt())
                },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Green,
                    activeTrackColor = Color.Green,
                    inactiveTrackColor = Color.LightGray,
                ),
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = viewModel.formatDuration(currentPosition.toLong()),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    color = Color.White
                )
                Text(
                    text = viewModel.formatDuration(duration.toLong()),
                    color = Color.White,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fast_forward),
                    contentDescription = "",
                    modifier = Modifier
                        .rotate(180f)
                        .padding(8.dp)
                        .clickable {
                            val newPosition = (player.currentPosition - 10000).coerceAtLeast(0)
                            player.seekTo(newPosition)
                        }

                )
                Image(
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (currentSongIndex > 0) {
                                onSongChange(currentSongIndex - 1)
                                playSong(currentSongIndex - 1)
                            }
                        }
                )

                Image(
                    painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (isPlaying) {
                                player.pause()
                            } else {
                                player.start()
                            }
                            isPlaying = !isPlaying
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (currentSongIndex < songs.size - 1) {
                                onSongChange(currentSongIndex + 1)
                                playSong(currentSongIndex + 1)
                            }
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.fast_forward),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            val newPosition = (player.currentPosition + 10000).coerceAtMost(duration)
                            player.seekTo(newPosition)
                        }
                )
            }
        }
    }
}