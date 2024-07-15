package com.example.musicplayer.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.musicplayer.MainActivity
import com.example.musicplayer.R
import com.example.musicplayer.data.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicService : Service() {


    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var currentSong: Song? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    fun playSong(song: Song) {
        currentSong = song
        mediaPlayer?.reset()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.path)
            prepare()
            start()
            setOnCompletionListener {
                stopSelf()
            }
        }
        showNotification()
        updateProgress()
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                it.start()
            }
            showNotification()
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    private fun updateProgress() {
        scope.launch {
            while (isPlaying()) {
                _currentPosition.value = mediaPlayer?.currentPosition ?: 0
                delay(1000)
            }
        }
    }

    private fun showNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music_channel",
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val playPauseAction = NotificationCompat.Action(
            if (mediaPlayer?.isPlaying == true) R.drawable.pause else R.drawable.play,
            if (mediaPlayer?.isPlaying == true) "Pause" else "Play",
            PendingIntent.getService(this, 0, Intent(this, MusicService::class.java).apply {
                action = if (mediaPlayer?.isPlaying == true) "ACTION_PAUSE" else "ACTION_PLAY"
            }, PendingIntent.FLAG_UPDATE_CURRENT)
        )

        val notification = NotificationCompat.Builder(this, "music_channel")
            .setContentTitle(currentSong?.name)
            .setContentText(currentSong?.singerName)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(currentSong?.image?.let { BitmapFactory.decodeFile(it.toString()) })
            .setContentIntent(
                PendingIntent.getActivity(
                    this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .addAction(playPauseAction)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0)) // Use androidx.media.app.NotificationCompat.MediaStyle            .build()
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        scope.cancel()
        super.onDestroy()
    }
}