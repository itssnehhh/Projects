package com.example.musicplayer.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.example.musicplayer.MainActivity
import com.example.musicplayer.R
import com.example.musicplayer.data.model.NotificationMusicPlayEventChange
import com.example.musicplayer.data.model.Song
import com.example.musicplayer.data.service.CONSTANT.CHANNEL_ID
import com.example.musicplayer.data.service.CONSTANT.NEXT_ACTION
import com.example.musicplayer.data.service.CONSTANT.NEXT_CODE
import com.example.musicplayer.data.service.CONSTANT.PAUSE_ACTION
import com.example.musicplayer.data.service.CONSTANT.PLAY_ACTION
import com.example.musicplayer.data.service.CONSTANT.PREVIOUS_ACTION
import com.example.musicplayer.data.service.CONSTANT.PREV_CODE
import com.example.musicplayer.data.service.CONSTANT.REQ_CODE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicService @Inject constructor() : Service() {

    private val binder = MusicBinder()
    var mediaPlayer: MediaPlayer? = null
    private var currentSong: Song? = null
    private var currentSongIndex = -1
    private var songList: List<Song> = emptyList()
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val _currentPosition = MutableStateFlow(0)

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MusicService", "onStartCommand called with action: ${intent?.action}")
        when (intent?.action) {
            PLAY_ACTION, PAUSE_ACTION -> playPauseSong()
            NEXT_ACTION -> playNext()
            PREVIOUS_ACTION -> playPrevious()
        }
        return START_NOT_STICKY
    }

    fun playSong(song: Song) {
        currentSong = song
        mediaPlayer?.reset()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.path)
            prepare()
            start()
            setOnCompletionListener {
                playNext()
            }
        }
        showNotification()
        updateProgress()
        Log.d("MusicService", "Playing song: ${song.name}")
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                it.start()
            }
            EventBus.getDefault().post(NotificationMusicPlayEventChange(
                isPlaying(),
                currentSongIndex
            ))
            showNotification()
            Log.d("MusicService", "Toggled play/pause, isPlaying: ${it.isPlaying}")
        }
    }

    private fun playPauseSong() {
        togglePlayPause()
    }

    private fun playNext() {
        Log.d("MusicService", "Attempting to play next song. Current index: $currentSongIndex, Song list size: ${songList.size}")
        if (songList.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songList.size
            playSongAtIndex(currentSongIndex)
            notifySongChange()
            Log.d("MusicService", "Playing next song, Index: $currentSongIndex")
        } else {
            Log.d("MusicService", "Song list is empty, cannot play next song")
        }
    }

    private fun playSongAtIndex(index: Int) {
        Log.d("MusicService", "Attempting to play song at index: $index")
        if (songList.isNotEmpty() && index >= 0 && index < songList.size) {
            currentSongIndex = index
            playSong(songList[index])
            notifySongChange()
            Log.d("MusicService", "Playing song at index: $index")
        } else {
            Log.d("MusicService", "Invalid index or empty song list. Index: $index, Song List Size: ${songList.size}")
        }
    }
    private fun playPrevious() {
        if (songList.isNotEmpty()) {
            currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else songList.size - 1
            playSongAtIndex(currentSongIndex)
            Log.d("MusicService", "Playing previous song, Index: $currentSongIndex")
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        Log.d("MusicService", "Seeking to position: $position")
    }

    private fun updateProgress() {
        scope.launch {
            while (isPlaying()) {
                _currentPosition.value = mediaPlayer?.currentPosition ?: 0
                delay(1000)
            }
        }
    }

    private fun notifySongChange() {
        EventBus.getDefault().post(NotificationMusicPlayEventChange(isPlaying(), currentSongIndex))
    }

    private fun showNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Music Playback", NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val playPauseAction = NotificationCompat.Action(
            if (mediaPlayer?.isPlaying == true) R.drawable.pause else R.drawable.play,
            if (mediaPlayer?.isPlaying == true) "Pause" else "Play",
            PendingIntent.getService(
                this,
                REQ_CODE,
                Intent(this, MusicService::class.java).apply {
                    action = if (mediaPlayer?.isPlaying == true) PAUSE_ACTION else PLAY_ACTION
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        val nextSong = NotificationCompat.Action(
            R.drawable.next,
            "Next",
            PendingIntent.getService(this, NEXT_CODE, Intent(this, MusicService::class.java).apply {
                action = NEXT_ACTION
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        )

        val prevSong = NotificationCompat.Action(
            R.drawable.previous,
            "Previous",
            PendingIntent.getService(this, PREV_CODE, Intent(this, MusicService::class.java).apply {
                action = PREVIOUS_ACTION
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentSong?.name)
            .setContentText(currentSong?.singerName)
            .setSmallIcon(R.drawable.music)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    REQ_CODE,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .addAction(prevSong)
            .addAction(playPauseAction)
            .addAction(nextSong)
            .setStyle(MediaStyle().setShowActionsInCompactView(0, 1, 2))
            .build()

        startForeground(1, notification)
        Log.d("MusicService", "Showing notification for song: ${currentSong?.name}")
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        scope.cancel()
        super.onDestroy()
        Log.d("MusicService", "Service destroyed")
    }

    fun setSongList(songs: List<Song>) {
        songList = songs
        currentSongIndex = -1
        Log.d("MusicService", "Song list set. Size: ${songs.size}")
    }
}

object CONSTANT {
    const val PLAY_ACTION = "ACTION_PLAY"
    const val PAUSE_ACTION = "ACTION_PAUSE"
    const val NEXT_ACTION = "NEXT_SONG"
    const val PREVIOUS_ACTION = "PREVIOUS_SONG"
    const val REQ_CODE = 0
    const val NEXT_CODE = 1
    const val PREV_CODE = 2
    const val CHANNEL_ID = "music_channel"
}