package com.example.musicplayer.ui.screen.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.musicplayer.R
import com.example.musicplayer.data.service.MusicService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val context = LocalContext.current
    val songList by viewModel.songList.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val selectedSongIndex by viewModel.selectedSongIndex.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val durationValue = duration.toFloat().coerceAtLeast(1f) // Avoid division by zero
    val currentPositionValue = currentPosition.toFloat().coerceAtLeast(0f)

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            if (duration > 0) {
                sliderPosition = (currentPosition.toFloat() / durationValue * 100).coerceIn(0f..100f)
            }
            delay(1000)
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            if (selectedSongIndex >= 0) {
                val song = songList[selectedSongIndex]
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxSize()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = song.image),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(16.dp)
                            .aspectRatio(1f)
                    )
                    Text(
                        text = song.name,
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
                        text = song.singerName,
                        maxLines = 1,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    )

                    Slider(
                        value = sliderPosition, // Use sliderPosition instead of currentPositionValue
                        onValueChange = { value ->
                            sliderPosition = value
                            viewModel.seekTo((value / 100 * duration).toInt())
                        },
                        valueRange = 0f..durationValue,
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
                                    viewModel.seekTo((currentPosition - 10000).coerceAtLeast(0))
                                }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.previous),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    if (selectedSongIndex > 0) {
                                        viewModel.playSong(selectedSongIndex - 1)
                                    }
                                }
                        )
                        Image(
                            painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    viewModel.togglePlayPause()
                                }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.next),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    if (selectedSongIndex < songList.size - 1) {
                                        viewModel.playSong(selectedSongIndex + 1)
                                    }
                                }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.fast_forward),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    viewModel.seekTo((currentPosition + 10000).coerceAtMost(duration))
                                }
                        )
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(paddingValues)
            ) {
                items(songList) { song ->
                    MusicCard(song, viewModel,  isPlaying = selectedSongIndex == songList.indexOf(song)) {
                        viewModel.playSong(songList.indexOf(song))
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                        context.startService(Intent(context,MusicService::class.java))
                    }
                }
            }

            if (selectedSongIndex >= 0) {
                val song = songList[selectedSongIndex]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.DarkGray)
                        .clickable {
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = song.image),
                        contentDescription = "",
                        modifier = Modifier
                            .size(48.dp)
                            .aspectRatio(1f)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = song.name,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = song.singerName,
                            maxLines = 1,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Image(
                        painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                viewModel.togglePlayPause()
                            }
                    )
                }
            }
        }
    }
}