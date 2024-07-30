package com.example.chatapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.constants.CONSTANTS.LOGIN_SCREEN
import com.example.chatapplication.constants.CONSTANTS.MAIN_SCREEN
import com.example.chatapplication.constants.CONSTANTS.SIGN_UP_SCREEN
import com.example.chatapplication.ui.screen.MainScreen
import com.example.chatapplication.ui.screen.login.LoginScreen
import com.example.chatapplication.ui.screen.signup.SignUpScreen
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by rememberSaveable { mutableStateOf(false) }
            ChatApplicationTheme {
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

