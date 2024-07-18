package com.example.etchatapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.etchatapplication.CONSTANTS.GROUP_SCREEN
import com.example.etchatapplication.CONSTANTS.HOME_SCREEN
import com.example.etchatapplication.CONSTANTS.SETTINGS_SCREEN
import com.example.etchatapplication.R
import com.example.etchatapplication.model.BottomNavItem
import com.example.etchatapplication.ui.screen.group.GroupScreen
import com.example.etchatapplication.ui.screen.home.HomeScreen
import com.example.etchatapplication.ui.screen.settings.SettingsScreen

@Composable
fun MainScreen(darkTheme: Boolean, darkThemeChange: () -> Unit) {

    val innerNavController = rememberNavController()

    Surface {
        Scaffold(
            topBar = {
                Text(text = "Talk Hub")
            },
            bottomBar = {
                BottomNavigationBar(navController = innerNavController)
            }
        ) { paddingValues ->
            NavHostContainer(
                navController = innerNavController,
                padding = paddingValues,
                darkTheme = darkTheme,
                darkThemeChange = darkThemeChange
            )
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    darkTheme: Boolean,
    darkThemeChange: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN,
        modifier = Modifier.padding(padding)
    ) {
        composable(HOME_SCREEN) {
            HomeScreen()
        }
        composable(GROUP_SCREEN) {
            GroupScreen()
        }
        composable(SETTINGS_SCREEN) {
            SettingsScreen(darkTheme,darkThemeChange)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItem = listOf(
        BottomNavItem(
            label = "Groups",
            icon = R.drawable.group,
            route = GROUP_SCREEN
        ),
        BottomNavItem(
            label = "Home",
            icon = R.drawable.home,
            route = HOME_SCREEN
        ),
        BottomNavItem(
            label = "Settings",
            icon = R.drawable.settings,
            route = SETTINGS_SCREEN
        )
    )

    BottomAppBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItem.forEach { bottomNavItem ->
            NavigationBarItem(
                selected = currentRoute == bottomNavItem.route,
                onClick = { navController.navigate(bottomNavItem.route) },
                icon = {
                    Image(
                        painter = painterResource(id = bottomNavItem.icon),
                        contentDescription = bottomNavItem.label
                    )
                },
                label = { Text(text = bottomNavItem.label) },
                alwaysShowLabel = false
            )
        }
    }
}
