package com.example.cpmathquestquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cpmathquestquizapp.ui.theme.CPMathQuestQuizAppTheme
import com.example.cpmathquestquizapp.screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember {
                mutableStateOf(false)
            }
            CPMathQuestQuizAppTheme(darkTheme) {
                MainScreen(darkTheme){
                    darkTheme = !darkTheme
                }
            }
        }
    }
}
