package com.example.cpemployeehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cpemployeehub.ui.screen.MainScreen
import com.example.employeehubapplication.ui.theme.EmployeeHubApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmployeeHubApplicationTheme {
                MainScreen()
            }
        }
    }
}

