package com.example.etchatapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.etchatapplication.CONSTANTS.CHAT_SCREEN
import com.example.etchatapplication.CONSTANTS.GROUP_ADD_SCREEN
import com.example.etchatapplication.CONSTANTS.GROUP_DETAIL_SCREEN
import com.example.etchatapplication.CONSTANTS.GROUP_SCREEN
import com.example.etchatapplication.CONSTANTS.HOME_SCREEN
import com.example.etchatapplication.CONSTANTS.SETTINGS_SCREEN
import com.example.etchatapplication.CONSTANTS.USERS_LIST_SCREEN
import com.example.etchatapplication.R
import com.example.etchatapplication.model.BottomNavItem
import com.example.etchatapplication.ui.screen.chat.ChatScreen
import com.example.etchatapplication.ui.screen.group.list.GroupScreen
import com.example.etchatapplication.ui.screen.group.add.GroupAddScreen
import com.example.etchatapplication.ui.screen.group.chat.GroupChatScreen
import com.example.etchatapplication.ui.screen.group.detail.GroupDetailScreen
import com.example.etchatapplication.ui.screen.home.HomeScreen
import com.example.etchatapplication.ui.screen.settings.SettingsScreen
import com.example.etchatapplication.ui.screen.users.UserListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, darkTheme: Boolean, darkThemeChange: () -> Unit) {

    val innerNavController = rememberNavController()
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface {
        Scaffold(
            topBar = {
                if (currentRoute !in listOf(
                        USERS_LIST_SCREEN, "$CHAT_SCREEN/{userId}", GROUP_ADD_SCREEN
                    )
                ) {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.talk_hub)) },
                        colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2BCA8D))
                    )
                }
            },
            bottomBar = {
                if (currentRoute !in listOf(
                        USERS_LIST_SCREEN, "$CHAT_SCREEN/{userId}", GROUP_ADD_SCREEN
                    )
                ) {
                    BottomNavigationBar(navController = innerNavController)
                }
            }
        ) { paddingValues ->
            NavHostContainer(
                navController = navController,
                innerNavController = innerNavController,
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
    innerNavController: NavHostController,
    padding: PaddingValues,
    darkTheme: Boolean,
    darkThemeChange: () -> Unit,
) {
    NavHost(
        navController = innerNavController,
        startDestination = HOME_SCREEN,
        modifier = Modifier.padding(padding)
    ) {
        composable(HOME_SCREEN) {
            HomeScreen(innerNavController)
        }
        composable(GROUP_SCREEN) {
            GroupScreen(innerNavController)
        }
        composable(SETTINGS_SCREEN) {
            SettingsScreen(navController, darkTheme, darkThemeChange)
        }
        composable(route = USERS_LIST_SCREEN) {
            UserListScreen(innerNavController)
        }
        composable("$CHAT_SCREEN/{userId}") { navBackStack ->
            val userId = navBackStack.arguments?.getString("userId")
            ChatScreen(innerNavController, userId)
        }
        composable(GROUP_ADD_SCREEN) {
            GroupAddScreen(innerNavController)
        }
        composable(GROUP_DETAIL_SCREEN) {
            GroupDetailScreen(innerNavController)
        }
        composable(
            "group_chat_screen/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
            ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            GroupChatScreen(groupId = groupId , innerNavController = innerNavController )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItem = listOf(
        BottomNavItem(
            label = stringResource(R.string.groups),
            icon = R.drawable.group,
            route = GROUP_SCREEN
        ),
        BottomNavItem(
            label = stringResource(R.string.home),
            icon = R.drawable.home,
            route = HOME_SCREEN
        ),
        BottomNavItem(
            label = stringResource(id = R.string.settings),
            icon = R.drawable.settings,
            route = SETTINGS_SCREEN
        )
    )

    BottomAppBar(
        containerColor = Color.LightGray,
        contentColor = Color.White,
        tonalElevation = 8.dp
    ) {
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
                label = { Text(text = bottomNavItem.label, color = Color.Black) },
                alwaysShowLabel = false
            )
        }
    }
}

