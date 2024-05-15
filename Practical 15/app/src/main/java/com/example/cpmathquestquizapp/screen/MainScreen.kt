package com.example.cpmathquestquizapp.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cpmathquestquizapp.model.BottomNavItem

@Composable
fun MainScreen(darkTheme: Boolean, function: () -> Unit) {

    val navController = rememberNavController()

        Surface(color = Color.White) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }, content = {
                    NavHostContainer(navController = navController, padding = it,darkTheme,function)
                }
            )
    }
}

object Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        BottomNavItem(
            label = "Leaderboard",
            icon = Icons.Filled.List,
            route = "leaderboard"
        ),
        BottomNavItem(
            label = "Notification",
            icon = Icons.Filled.Notifications,
            route = "notification"
        ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Filled.Person,
            route = "profile"
        )
    )
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    darkTheme: Boolean,
    function: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues = padding),
        builder = {
            composable("home") {
                HomeScreen(navController)
            }
            composable("leaderboard") {
                LeaderBoardScreen()
            }
            composable("notification") {
                NotificationScreen()
            }
            composable("profile") {
                ProfileScreen(darkTheme,function)
            }
            composable("quizStart") {
                QuizScreen(navController)
            }
        })
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    BottomAppBar(
        containerColor = Color(0xFFB6FDB9)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Constants.BottomNavItems.forEach { navItem ->

            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                },
                label = {
                    Text(text = navItem.label)
                },
                alwaysShowLabel = false
            )
        }
    }
}