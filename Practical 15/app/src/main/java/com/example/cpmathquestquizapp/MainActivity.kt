package com.example.mathquestquizapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mathquestquizapplication.screen.MainScreen
import com.example.mathquestquizapplication.ui.theme.MathQuestQuizApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var darkTheme by remember {
                mutableStateOf(false)
            }

            MathQuestQuizApplicationTheme (darkTheme){
                MainScreen(darkTheme){
                    darkTheme = !darkTheme
                }
            }
        }
    }
}