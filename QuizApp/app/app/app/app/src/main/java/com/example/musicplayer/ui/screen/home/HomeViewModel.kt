package com.example.musicplayer.ui.screen.home

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.R
import com.example.musicplayer.data.model.Song
import com.example.musicplayer.data.service.MusicService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _songList = MutableStateFlow<List<Song>>(emptyList())
    val songList: StateFlow<List<Song>> = _songList

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _selectedSongIndex = MutableStateFlow(-1)
    val selectedSongIndex: StateFlow<Int> = _selectedSongIndex

    private var musicService: MusicService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
        }
    }

    init {
        fetchSongList(application)
        val intent = Intent(application, MusicService::class.java)
        application.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun fetchSongList(application: Application) {
        viewModelScope.launch {
            val songList = mutableListOf<Song>()
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
            )
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

            val query = application.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
            )

            query?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val artist = cursor.getString(artistColumn)
                    val album = cursor.getString(albumColumn)
                    val duration = cursor.getLong(durationColumn)
                    val data = cursor.getString(dataColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    val bitmap = getAlbumArt(application, contentUri)
                    Log.d("HomeViewModel", "Song fetched: $title by $artist")

                    songList.add(
                        Song(
                            id = id.toInt(),
                            name = title,
                            singerName = artist,
                            image = bitmap,
                            path = data,
                            album = album,
                            duration = duration
                        )
                    )
                }
            }
            _songList.value = songList
            Log.d("HomeViewModel", "Total songs fetched: ${songList.size}")
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatDuration(duration: Long): String {
        val minutes = (duration / 1000) / 60
        val seconds = (duration / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getAlbumArt(context: Context, uri: Uri): Bitmap {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(context, uri)
        val data = mmr.embeddedPicture
        return if (data != null) {
            BitmapFactory.decodeByteArray(data, 0, data.size)
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground)
        }
    }

    fun playSong(songIndex: Int) {
        _selectedSongIndex.value = songIndex
        val song = _songList.value[songIndex]
        musicService?.playSong(song)
        _isPlaying.value = true
    }

    fun togglePlayPause() {
        if (isBound) {
            musicService?.togglePlayPause()
            _isPlaying.value = musicService?.isPlaying() == true
        }
    }

    fun seekTo(position: Int) {
        if (isBound) {
            musicService?.seekTo(position)
        }
    }

    override fun onCleared() {
        if (isBound) {
            getApplication<Application>().unbindService(serviceConnection)
        }
        super.onCleared()
    }

}