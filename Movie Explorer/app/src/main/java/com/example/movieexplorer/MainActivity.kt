package com.example.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.movieexplorer.screen.MovieAppScreen
import com.example.movieexplorer.ui.theme.MovieExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by remember { mutableStateOf(false) }

            MovieExplorerTheme(darkTheme) {
                MovieAppScreen(darkTheme) {
                    darkTheme = !darkTheme
                }
            }
        }
    }
}
