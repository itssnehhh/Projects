package com.example.musicplayer.data.model

data class NotificationMusicPlayEventChange(
    val isPlaying: Boolean = false,
    val currentSongIndex: Int
)
