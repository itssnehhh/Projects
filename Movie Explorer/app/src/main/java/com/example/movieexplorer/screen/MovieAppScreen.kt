package com.example.movieexplorer.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieexplorer.constants.Constants.HOME_SCREEN
import com.example.movieexplorer.constants.Constants.LOGIN_SCREEN
import com.example.movieexplorer.constants.Constants.MAIN_SCREEN
import com.example.movieexplorer.constants.Constants.ON_BOARD_SCREEN
import com.example.movieexplorer.constants.Constants.SIGNUP_SCREEN
import com.example.movieexplorer.preferences.PrefManager

@Composable
fun MovieAppScreen(darkTheme: Boolean, function: () -> Unit) {

    val context = LocalContext.current

    val navController = rememberNavController()
    val prefManager = remember { PrefManager(context) }

    SetUpNavGraph(navController, prefManager, darkTheme, function)
}

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    prefManager: PrefManager,
    darkTheme: Boolean,
    function: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination =
        if (prefManager.getLoginStatus()) {
            MAIN_SCREEN
        } else {
            if (prefManager.getOnBoardingStatus()) {
                LOGIN_SCREEN
            } else {
                ON_BOARD_SCREEN
            }
        }
    ) {
        composable(ON_BOARD_SCREEN) {
            OnBoardScreen(navController = navController)
        }
        composable(LOGIN_SCREEN) {
            LoginScreen(navController = navController)
        }
        composable(SIGNUP_SCREEN) {
            SignUpScreen(navController = navController)
        }
        composable(MAIN_SCREEN) {
            MainScreen(navController = navController, darkTheme, function)
        }
    }
}
