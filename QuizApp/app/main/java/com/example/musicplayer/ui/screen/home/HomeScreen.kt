package com.example.musicplayer.ui.screen.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicplayer.R
import com.example.musicplayer.data.service.MusicService
import com.example.musicplayer.ui.component.BottomSheetContent
import com.example.musicplayer.ui.component.MiniPlayer
import com.example.musicplayer.ui.component.MusicCard
import com.example.musicplayer.ui.theme.CustomGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val viewModel = hiltViewModel<HomeViewModel>()
    val context = LocalContext.current
    val songList by viewModel.songList.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val selectedSongIndex by viewModel.selectedSongIndex.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CustomGreen)
            )
        }
    ) { values ->
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                if (selectedSongIndex >= 0) {
                    BottomSheetContent(
                        viewModel = viewModel,
                        currentPosition = currentPosition,
                        selectedSongIndex = selectedSongIndex,
                        isPlaying = isPlaying,
                        songList = songList,
                        duration = duration
                    )
                }
            },
            modifier = Modifier.padding(values),
            sheetPeekHeight = 0.dp,
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray)
                        .padding(paddingValues)
                ) {
                    items(songList) { song ->
                        MusicCard(
                            song = song,
                            homeViewModel = viewModel,
                            isPlaying = selectedSongIndex == songList.indexOf(song)
                        ) {
                            viewModel.playSong(songList.indexOf(song))
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                            context.startService(Intent(context, MusicService::class.java))
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
                            .background(CustomGreen)
                            .clickable {
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            }
                            .padding(paddingValues),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MiniPlayer(song, viewModel, isPlaying)
                    }
                }
            }
        }
    }
}
