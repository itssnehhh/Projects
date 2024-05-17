package com.example.cpimagedownloadapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cpimagedownloadapplication.screen.MainScreen
import com.example.cpimagedownloadapplication.ui.theme.ImageSaverApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageSaverApplicationTheme {
                MainScreen()
            }
        }

    }
}


