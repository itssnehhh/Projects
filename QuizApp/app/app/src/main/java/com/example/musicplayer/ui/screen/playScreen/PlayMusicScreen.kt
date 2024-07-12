package com.example.musicplayer.ui.screen.playScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.data.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayScreen(song: Song) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1DB954)),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(16.dp)
                        .aspectRatio(1f)
                )
                Text(
                    text = "Tujhe Kitna Chahne Lage Hum",
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
                Text(
                    text = "Arijit Singh and Mithoon",
                    maxLines = 1,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp)
                )

                Slider(
                    value = 50f,
                    onValueChange = { },
                    onValueChangeFinished = {},
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Green,
                        activeTrackColor = Color.Green,
                        inactiveTrackColor = Color.LightGray,
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.fast_forward),
                        contentDescription = "",
                        modifier = Modifier
                            .rotate(180f)
                            .padding(8.dp)

                    )
                    Image(
                        painter = painterResource(id = R.drawable.previous),
                        contentDescription = "",
                        modifier = Modifier.padding(8.dp)
                    )

                    var isPlaying by remember {
                        mutableStateOf(false)
                    }

                    Image(
                        painter = painterResource(id = if (isPlaying) R.drawable.play else R.drawable.pause),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { isPlaying = !isPlaying }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.next),
                        contentDescription = "",
                        modifier = Modifier.padding(8.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.fast_forward),
                        contentDescription = "",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MusicPlayScreenPreview() {
    MusicPlayScreen(Song(0,"","","","","",0L))
}