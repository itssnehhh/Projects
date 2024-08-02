package com.example.etchatapplication.ui.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.etchatapplication.R
import com.example.etchatapplication.constants.CONSTANTS.CHAT_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.GROUP_ADD_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.GROUP_CHAT_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.GROUP_DETAIL_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.GROUP_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.HOME_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.IMAGE_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.SETTINGS_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.TIME_DURATION
import com.example.etchatapplication.constants.CONSTANTS.USERS_LIST_SCREEN
import com.example.etchatapplication.model.BottomNavItem
import com.example.etchatapplication.ui.screen.chat.ChatScreen
import com.example.etchatapplication.ui.screen.group.add.GroupAddScreen
import com.example.etchatapplication.ui.screen.group.chat.GroupChatScreen
import com.example.etchatapplication.ui.screen.group.detail.GroupDetailScreen
import com.example.etchatapplication.ui.screen.group.list.GroupListScreen
import com.example.etchatapplication.ui.screen.home.HomeScreen
import com.example.etchatapplication.ui.screen.image.ImageViewScreen
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
                        USERS_LIST_SCREEN,
                        "$CHAT_SCREEN/{userId}",
                        GROUP_ADD_SCREEN,
                        "$GROUP_CHAT_SCREEN/{groupId}",
                        "$GROUP_DETAIL_SCREEN/{groupId}",
                        "$IMAGE_SCREEN/{imageUrl}"
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
                        USERS_LIST_SCREEN,
                        "$CHAT_SCREEN/{userId}",
                        GROUP_ADD_SCREEN,
                        "$GROUP_CHAT_SCREEN/{groupId}",
                        "$GROUP_DETAIL_SCREEN/{groupId}",
                        "$IMAGE_SCREEN/{imageUrl}"
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
    darkThemeChange: () -> Unit
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
            GroupListScreen(innerNavController)
        }
        composable(SETTINGS_SCREEN) {
            SettingsScreen(navController, darkTheme, darkThemeChange)
        }
        composable(
            route = USERS_LIST_SCREEN,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {
            UserListScreen(innerNavController)
        }
        composable(
            route = "$CHAT_SCREEN/{userId}",
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) { navBackStack ->
            val userId = navBackStack.arguments?.getString("userId")
            ChatScreen(innerNavController, userId)
        }
        composable(
            route = GROUP_ADD_SCREEN,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {
            GroupAddScreen(innerNavController)
        }
        composable(
            route = "$GROUP_DETAIL_SCREEN/{groupId}",
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) { navBackStack ->
            val groupId = navBackStack.arguments?.getString("groupId")
            if (groupId != null) {
                GroupDetailScreen(innerNavController, groupId)
            }
        }
        composable(
            route = "$GROUP_CHAT_SCREEN/{groupId}",
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) { navBackStack ->
            val groupId = navBackStack.arguments?.getString("groupId")
            if (groupId != null) {
                GroupChatScreen(innerNavController, groupId)
            }
        }
        composable("$IMAGE_SCREEN/{imageUrl}") { navBackStack ->
            val imageUrl = navBackStack.arguments?.getString("imageUrl")
            if (imageUrl != null) {
                ImageViewScreen(imageUrl = imageUrl, innerNavController = innerNavController)
            }
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

val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    fadeIn(animationSpec = tween(TIME_DURATION, easing = LinearEasing)) +
            slideIntoContainer(
                animationSpec = tween(TIME_DURATION, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
}

val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    fadeOut(animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearEasing)) +
            slideOutOfContainer(
                animationSpec = tween(durationMillis = TIME_DURATION, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
}
