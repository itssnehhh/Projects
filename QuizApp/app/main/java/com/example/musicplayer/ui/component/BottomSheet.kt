package com.example.musicplayer.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.musicplayer.R
import com.example.musicplayer.data.model.Song
import com.example.musicplayer.ui.screen.home.HomeViewModel

@Composable
fun BottomSheetContent(
    viewModel: HomeViewModel,
    currentPosition: Int,
    selectedSongIndex: Int,
    isPlaying: Boolean,
    songList: List<Song>,
    duration: Int
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val durationValue = duration.toFloat().coerceAtLeast(1f)
    val currentPositionValue = currentPosition.toFloat().coerceAtLeast(0f)
    val song = songList[selectedSongIndex]

    LaunchedEffect(currentPosition) {
        if (duration > 0) {
            sliderPosition = (currentPositionValue / durationValue * 100).coerceIn(0f..100f)
        }
    }

    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = song.image),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.4f),
            contentScale = ContentScale.Crop
        )
        LazyColumn(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
            item {
                Image(
                    painter = rememberAsyncImagePainter(model = song.image),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(32.dp)
                        .aspectRatio(12f / 9f)
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
                    value = sliderPosition,
                    onValueChange = { value ->
                        sliderPosition = value
                        viewModel.seekTo(((sliderPosition / 100) * durationValue).toInt())
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
                        text = formatTime(currentPositionValue.toInt()),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        color = Color.White
                    )
                    Text(
                        text = formatTime(durationValue.toInt()),
                        color = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                PlayerControls(
                    isPlaying = isPlaying,
                    onPrevious = { viewModel.playPrevious() },
                    onNext = { viewModel.playNext() },
                    onTogglePlayPause = { viewModel.togglePlayPause() },
                    onForward = { viewModel.forwardTenSeconds() },
                    onBackward = { viewModel.backwardTenSeconds() },
                    viewModel
                )
            }
        }
    }
}

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onForward: () -> Unit,
    onBackward: () -> Unit,
    viewModel: HomeViewModel
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onBackward) {
            Image(
                painter = painterResource(id = R.drawable.fast_forward),
                contentDescription = "",
                modifier = Modifier
                    .size(36.dp)
                    .rotate(180f)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(onClick = onPrevious) {
            Image(
                painter = painterResource(id = R.drawable.previous),
                contentDescription = "",
                modifier = Modifier.size(48.dp)
            )
        }

        IconButton(onClick = onTogglePlayPause) {
            Image(
                painter = painterResource(if (isPlaying) R.drawable.pause else R.drawable.play),
                contentDescription = "",
                modifier = Modifier.size(48.dp)
            )
        }
        IconButton(onClick = onNext) {
            Image(
                painter = painterResource(id = R.drawable.next),
                contentDescription = "",
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(onClick = onForward) {
            Image(
                painter = painterResource(R.drawable.fast_forward),
                contentDescription = "",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(milliseconds: Int): String {
    val minutes = milliseconds / 1000 / 60
    val seconds = milliseconds / 1000 % 60
    return String.format("%02d:%02d", minutes, seconds)
}