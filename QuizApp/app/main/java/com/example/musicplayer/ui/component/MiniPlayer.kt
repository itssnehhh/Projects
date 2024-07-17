package com.example.musicplayer.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.musicplayer.R
import com.example.musicplayer.data.model.Song
import com.example.musicplayer.ui.screen.home.HomeViewModel

@Composable
fun MiniPlayer(
    song: Song,
    viewModel: HomeViewModel,
    isPlaying: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = song.image),
            contentDescription = "",
            modifier = Modifier
                .size(80.dp)
                .aspectRatio(1f)
                .padding(4.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        ) {
            Text(
                text = song.name,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = song.singerName,
                maxLines = 1,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Image(painter = painterResource(id = R.drawable.previous),
            contentDescription = "",
            modifier = Modifier
                .padding(8.dp)
                .clickable { viewModel.playPrevious() }
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
        Image(painter = painterResource(id = R.drawable.next),
            contentDescription = "",
            modifier = Modifier
                .padding(8.dp)
                .clickable { viewModel.playNext() }
        )
    }
}