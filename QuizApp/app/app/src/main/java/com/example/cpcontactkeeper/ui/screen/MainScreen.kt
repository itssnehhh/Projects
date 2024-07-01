package com.example.cpcontactkeeper.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cpcontactkeeper.ui.screen.CONSTANTS.ADD_SCREEN
import com.example.cpcontactkeeper.ui.screen.CONSTANTS.DETAIL_SCREEN
import com.example.cpcontactkeeper.ui.screen.CONSTANTS.HOME_SCREEN
import com.example.cpcontactkeeper.viewModel.ContactViewModel

@Composable
fun MainScreen(viewModel: ContactViewModel) {
    val navController = rememberNavController()
    NavHostContainer(navController, viewModel)
}

@Composable
fun NavHostContainer(navController: NavHostController, viewModel: ContactViewModel) {

    NavHost(navController = navController, startDestination = HOME_SCREEN) {
        composable(HOME_SCREEN) {
            HomeScreen(navController,viewModel)
        }
        composable(
            route = DETAIL_SCREEN,
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId") ?: return@composable
            DetailScreen(navController, viewModel, contactId)
        }
        composable(ADD_SCREEN) {
            ContactAddScreen(contactViewModel = viewModel, navController)
        }
    }
}

object CONSTANTS {
    const val HOME_SCREEN = "homeScreen"
    const val DETAIL_SCREEN = "detailScreen/{contactId}"
    const val ADD_SCREEN = "contactAddScreen"

    fun detailScreenRoute(contactId: String) = "detailScreen/$contactId"
}