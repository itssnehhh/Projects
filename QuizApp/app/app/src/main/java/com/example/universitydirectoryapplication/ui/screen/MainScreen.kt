package com.example.universitydirectoryapplication.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.universitydirectoryapplication.ui.screen.Constant.HOME_SCREEN
import com.example.universitydirectoryapplication.ui.screen.Constant.SEARCH_SCREEN
import com.example.universitydirectoryapplication.ui.screen.Constant.WEB_SCREEN
import com.example.universitydirectoryapplication.ui.screen.home.HomeScreen
import com.example.universitydirectoryapplication.ui.screen.home.HomeViewModel
import com.example.universitydirectoryapplication.ui.screen.search.SearchScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHostContainer(navController)
}

@Composable
fun NavHostContainer(navController: NavHostController) {

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = HOME_SCREEN) {
        composable(HOME_SCREEN) {
            val homeViewModel = HomeViewModel(context)
            HomeScreen(navController, homeViewModel)
        }
        composable(SEARCH_SCREEN) {
            SearchScreen(navController)
        }
        composable("$WEB_SCREEN/{universityLink}") { navBackStack ->
            val universityLink = navBackStack.arguments?.getString("universityLink")
            WebViewScreen(universityLink)
        }
    }
}

object Constant {
    const val HOME_SCREEN = "homeScreen"
    const val WEB_SCREEN = "webScreen"
    const val SEARCH_SCREEN = "searchScreen"
}
