package com.example.musicplayer.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.data.model.Song
import com.example.musicplayer.ui.screen.Constants.HOME_SCREEN
import com.example.musicplayer.ui.screen.Constants.PLAY_SCREEN
import com.example.musicplayer.ui.screen.home.HomeScreen
import com.example.musicplayer.ui.screen.playScreen.MusicPlayScreen

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    NavHostContainer(navController)
}

@Composable
fun NavHostContainer(navController: NavHostController) {

    NavHost(navController = navController, startDestination = HOME_SCREEN) {
        composable(HOME_SCREEN){
            HomeScreen(navController)
        }
        composable(PLAY_SCREEN){
            val song = Song(0,"","","","","",0L)
            MusicPlayScreen(song)
        }
    }
}

object Constants{
    const val HOME_SCREEN = "homeScreen"
    const val PLAY_SCREEN = "playScreen"
}
