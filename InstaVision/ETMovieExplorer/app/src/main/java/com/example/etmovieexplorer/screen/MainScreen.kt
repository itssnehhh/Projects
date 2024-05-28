package com.example.etmovieexplorer.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.etmovieexplorer.preferences.PrefManager
import com.example.etmovieexplorer.screen.Constants.HOME
import com.example.etmovieexplorer.screen.Constants.LOGIN
import com.example.etmovieexplorer.screen.Constants.ON_BOARD
import com.example.etmovieexplorer.screen.Constants.SIGNUP

@Composable
fun MainScreen(darkTheme: Boolean, function: () -> Unit) {

    val context = LocalContext.current

    val navController = rememberNavController()
    val prefManager = remember { PrefManager(context) }

    SetUpNavGraph(navController, prefManager,darkTheme,function)
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
            HOME
        } else {
            if (prefManager.getOnBoardingStatus()) {
                LOGIN
            } else {
                ON_BOARD
            }
        }
    ) {
        composable(ON_BOARD) {
            OnBoardScreen(navController = navController)
        }
        composable(LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(SIGNUP) {
            SignUpScreen(navController = navController)
        }
        composable(HOME) {
            HomeScreen(navHostController = navController,darkTheme,function)
        }
    }
}

object Constants {
    const val HOME = "home"
    const val ON_BOARD = "onboard"
    const val LOGIN = "login"
    const val SIGNUP = "signUp"
}