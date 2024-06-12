package com.example.etmovieexplorer.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController) {
    Text(text = "Profile Screen", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
}