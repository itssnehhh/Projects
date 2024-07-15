package com.example.musicplayer.data.model

import android.graphics.Bitmap

data class Song(
    val id: Int,
    val name: String,
    val singerName: String,
    val image: Bitmap,
    val path: String,
    val album: String,
    val duration: Long,
    val isLiked: Boolean = false
)
