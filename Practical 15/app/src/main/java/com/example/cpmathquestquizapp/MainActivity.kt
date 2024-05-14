package com.example.cpmathquestquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cpmathquestquizapp.ui.theme.CPMathQuestQuizAppTheme
import com.example.cpmathquestquizapp.screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CPMathQuestQuizAppTheme {
                MainScreen()
            }
        }
    }
}
