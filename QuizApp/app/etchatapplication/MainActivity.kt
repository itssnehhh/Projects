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
import com.example.etchatapplication.CONSTANTS.LOGIN_SCREEN
import com.example.etchatapplication.CONSTANTS.MAIN_SCREEN
import com.example.etchatapplication.CONSTANTS.SIGN_UP_SCREEN
import com.example.etchatapplication.ui.screen.MainScreen
import com.example.etchatapplication.ui.screen.login.LoginScreen
import com.example.etchatapplication.ui.screen.signup.SignUpScreen
import com.example.etchatapplication.ui.theme.ETChatApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by rememberSaveable { mutableStateOf(false) }
            ETChatApplicationTheme(darkTheme) {
                NavRoute(darkTheme) {
                    darkTheme = !darkTheme
                }
            }
        }
    }
}

@Composable
fun NavRoute(darkTheme: Boolean, darkThemeChange: () -> Unit) {
    val navController = rememberNavController()
    val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null
    SetNavigationRoute(navController, isUserLoggedIn, darkTheme, darkThemeChange)
}

@Composable
fun SetNavigationRoute(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    darkTheme: Boolean,
    darkThemeChange: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) MAIN_SCREEN else LOGIN_SCREEN
    ) {
        composable(SIGN_UP_SCREEN) {
            SignUpScreen(navController)
        }
        composable(LOGIN_SCREEN) {
            LoginScreen(navController)
        }
        composable(MAIN_SCREEN) {
            MainScreen(navController, darkTheme, darkThemeChange)
        }
    }
}

object CONSTANTS {
    const val LOGIN_SCREEN = "loginScreen"
    const val SIGN_UP_SCREEN = "signInScreen"
    const val MAIN_SCREEN = "mainScreen"
    const val HOME_SCREEN = "homeScreen"
    const val GROUP_SCREEN = "groupScreen"
    const val SETTINGS_SCREEN = "settingsScreen"
    const val USERS_LIST_SCREEN = "usersListScreen"
}