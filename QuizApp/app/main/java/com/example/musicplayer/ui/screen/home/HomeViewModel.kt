package com.example.musicplayer.ui.screen.home

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.R
import com.example.musicplayer.data.model.NotificationMusicPlayEventChange
import com.example.musicplayer.data.model.Song
import com.example.musicplayer.data.service.MusicService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    private var musicService: MusicService? = null

    private val _songList = MutableStateFlow<List<Song>>(emptyList())
    val songList: StateFlow<List<Song>> = _songList

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _selectedSongIndex = MutableStateFlow(-1)
    var selectedSongIndex: StateFlow<Int> = _selectedSongIndex

    fun setMusicService(service: MusicService?) {
        musicService = service
        musicService?.setSongList(songList.value) // Set song list if service is already bound
    }

    init {
        EventBus.getDefault().register(this)
        fetchSongList(application)
        musicService?.setSongList(songList.value)
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
                    bitmap?.let {
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

    private fun getAlbumArt(context: Context, uri: Uri): Bitmap? {
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
        musicService?.playSong(songList.value[songIndex])
        _isPlaying.value = musicService?.isPlaying() ?: false
        _duration.value = musicService?.mediaPlayer?.duration ?: 0
    }

    fun togglePlayPause() {
        musicService?.togglePlayPause()
        _isPlaying.value = musicService?.isPlaying() ?: false
    }

    fun playPrevious() {
        val currentIndex = _selectedSongIndex.value
        if (currentIndex > 0) {
            playSong(currentIndex - 1)
        } else {
            playSong(songList.value.size - 1)
        }
    }

    fun playNext() {
        val currentIndex = _selectedSongIndex.value
        if (currentIndex < songList.value.size - 1) {
            playSong(currentIndex + 1)
        } else if (currentIndex == songList.value.size - 1) {
            playSong(0)
        }
    }

    fun seekTo(position: Int) {
        musicService?.seekTo(position)
    }


    fun forwardTenSeconds() {
        val currentPosition = musicService?.mediaPlayer?.currentPosition ?: 0
        musicService?.seekTo(currentPosition + 10000)
    }

    fun backwardTenSeconds() {
        val currentPosition = musicService?.mediaPlayer?.currentPosition ?: 0
        musicService?.seekTo(currentPosition - 10000)
    }

    fun observeMusicService() {
        viewModelScope.launch {
            while (true) {
                musicService?.let { it ->
                    (it.mediaPlayer?.currentPosition ?: 0).also { _currentPosition.value = it }
                    delay(1000)
                }
            }
        }
    }

    fun onStop() {
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMusicPlayEventChange(event: NotificationMusicPlayEventChange) {
        _isPlaying.value = event.isPlaying
        _selectedSongIndex.value = event.currentSongIndex // Update the selected song index
    }
}