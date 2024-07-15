package com.example.musicplayer.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.musicplayer.data.model.Song

@Composable
fun MusicCard(
    song: Song,
    homeViewModel: HomeViewModel,
    isPlaying: Boolean,
    onCardClick: () -> Unit,
) {

    var isLiked by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(Color(0xFFE3F5E3)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onCardClick() }
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            song.let {
                Image(
                    painter = rememberAsyncImagePainter(it.image),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(120.dp)
                        .background(Color.LightGray)
                )
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = it.name,
                        maxLines = 2,
                        color = if (isPlaying) Color.Green else Color.Black,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        text = it.singerName,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(4.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "",
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(
                            text = homeViewModel.formatDuration(it.duration), modifier = Modifier
                                .padding(4.dp)
                                .weight(1f)
                        )
                        Image(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(if (isLiked) Color.Red else Color.Black),
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { isLiked = !isLiked }
                        )
                    }
                }
            }
        }
    }
}
