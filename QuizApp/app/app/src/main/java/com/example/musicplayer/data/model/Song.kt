package com.example.musicplayer.data.model

data class Song(
    val id: Int,
    val name: String,
    val singerName: String,
    val image:String,
    val path:String,
    val album:String,
    val duration:Long,
    val isLiked: Boolean = false
)
