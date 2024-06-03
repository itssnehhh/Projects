package com.example.cpdrinkexplorer.screen

import MocktailViewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cpdrinkexplorer.screen.Constants.DETAIL_SCREEN
import com.example.cpdrinkexplorer.screen.Constants.FAVOURITE_SCREEN
import com.example.cpdrinkexplorer.screen.Constants.HOME_SCREEN
import com.example.cpdrinkexplorer.screen.Constants.SEARCH_SCREEN

@Composable
fun MainScreen(viewModel: MocktailViewModel) {
    Scaffold { paddingValues ->
        val navController = rememberNavController()
        NavHostContainer(navController = navController, padding = paddingValues,viewModel)
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    viewModel: MocktailViewModel
) {
    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN,
        modifier = Modifier.padding(padding)
    ) {
        composable(HOME_SCREEN) {
            HomeScreen(navController,viewModel)
        }
        composable(FAVOURITE_SCREEN) {
            FavouriteScreen(navController)
        }
        composable(SEARCH_SCREEN) {
            SearchScreen(navController,viewModel)
        }
        composable("$DETAIL_SCREEN/{mocktailId}") { backStackEntry ->
            val mocktailId = backStackEntry.arguments?.getString("mocktailId")
            mocktailId?.let {
                DetailScreen(mocktailId, viewModel,navController)
            }
        }
    }
}

object Constants {
    const val HOME_SCREEN = "homeScreen"
    const val SEARCH_SCREEN = "searchScreen"
    const val FAVOURITE_SCREEN = "favouriteScreen"
    const val DETAIL_SCREEN = "detailScreen"
}