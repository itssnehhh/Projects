package com.example.etchatapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.etchatapplication.CONSTANTS.INFO_SAVE_SCREEN
import com.example.etchatapplication.CONSTANTS.LOGIN_SCREEN
import com.example.etchatapplication.CONSTANTS.MAIN_SCREEN
import com.example.etchatapplication.CONSTANTS.SIGN_UP_SCREEN
import com.example.etchatapplication.ui.screen.MainScreen
import com.example.etchatapplication.ui.screen.login.LoginScreen
import com.example.etchatapplication.ui.screen.saveInfo.UserInfoSaveScreen
import com.example.etchatapplication.ui.screen.signup.SignUpScreen
import com.example.etchatapplication.ui.theme.ETChatApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by rememberSaveable { mutableStateOf(false) }
            ETChatApplicationTheme {
                NavRoute(darkTheme){
                    darkTheme = !darkTheme
                }
            }
        }
    }
}

@Composable
fun NavRoute(darkTheme: Boolean, darkThemeChange: () -> Unit) {
    val context = LocalContext.current
    val navController = rememberNavController()
    SetNavigationRoute(navController,darkTheme,darkThemeChange)
}

@Composable
fun SetNavigationRoute(
    navController: NavHostController,
    darkTheme: Boolean,
    darkThemeChange: () -> Unit
) {
    NavHost(navController = navController, startDestination = LOGIN_SCREEN) {
        composable(SIGN_UP_SCREEN) {
            SignUpScreen(navController)
        }
        composable(LOGIN_SCREEN) {
            LoginScreen(navController)
        }
        composable("$INFO_SAVE_SCREEN/{email}"){backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            UserInfoSaveScreen(navController, email)        }
        composable(MAIN_SCREEN) {
            MainScreen(darkTheme,darkThemeChange)
        }
    }
}

object CONSTANTS {
    const val LOGIN_SCREEN = "loginScreen"
    const val SIGN_UP_SCREEN = "signInScreen"
    const val MAIN_SCREEN = "mainScreen"
    const val INFO_SAVE_SCREEN = "infoSaveScreen"
    const val HOME_SCREEN = "homeScreen"
    const val GROUP_SCREEN = "groupScreen"
    const val SETTINGS_SCREEN = "settingsScreen"
}