package com.example.etmovieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.etmovieexplorer.screen.MainScreen
import com.example.etmovieexplorer.ui.theme.ETMovieExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var darkTheme by remember {
                mutableStateOf(false)
            }

            ETMovieExplorerTheme(darkTheme) {
                MainScreen(darkTheme) {
                    darkTheme = !darkTheme
                }
            }
        }
    }
}
