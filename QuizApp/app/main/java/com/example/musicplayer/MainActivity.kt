package com.example.musicplayer

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicplayer.data.model.Song
import com.example.musicplayer.data.service.MusicService
import com.example.musicplayer.ui.screen.home.HomeScreen
import com.example.musicplayer.ui.screen.home.HomeViewModel
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("PERMISSION", "Permission : Granted ")
        } else {
            Log.d("PERMISSION", "Permission : is Not Granted ")
        }
    }

    val homeViewModel: HomeViewModel by viewModels()

    private var musicService: MusicService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            homeViewModel.setMusicService(musicService) // Pass service to ViewModel
            isBound = true
            homeViewModel.observeMusicService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
        setContent {
            MusicPlayerTheme {
                HomeScreen()
            }

            val intent = Intent(this, MusicService::class.java)
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            startService(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }
}

