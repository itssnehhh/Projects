package com.example.cpcontactkeeper.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cpcontactkeeper.ui.screen.CONSTANTS.ADD_SCREEN
import com.example.cpcontactkeeper.ui.screen.CONSTANTS.DETAIL_SCREEN
import com.example.cpcontactkeeper.ui.screen.CONSTANTS.HOME_SCREEN
import com.example.cpcontactkeeper.ui.screen.addScreen.ContactAddScreen
import com.example.cpcontactkeeper.ui.screen.addScreen.ContactViewModel
import com.example.cpcontactkeeper.ui.screen.detail.DetailScreen
import com.example.cpcontactkeeper.ui.screen.detail.DetailViewModel
import com.example.cpcontactkeeper.ui.screen.home.HomeScreen
import com.example.cpcontactkeeper.ui.screen.home.HomeViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHostContainer(navController)
}

@Composable
fun NavHostContainer(navController: NavHostController) {

    val context = LocalContext.current
    val homeViewModel = HomeViewModel()
    val addViewModel = ContactViewModel(context)
    val detailViewModel = DetailViewModel()

    NavHost(navController = navController, startDestination = HOME_SCREEN) {
        composable(HOME_SCREEN) {
            HomeScreen(navController, homeViewModel)
        }
        composable("$ADD_SCREEN/{contactId}") { navBackStackEntry ->
            val contactId = navBackStackEntry.arguments?.getString("contactId")
            contactId?.let {
                ContactAddScreen(navController, addViewModel, it)
            }
        }
        composable("$DETAIL_SCREEN/{contactId}") { navBackStackEntry ->
            val contactId = navBackStackEntry.arguments?.getString("contactId")
            DetailScreen(navController, detailViewModel, contactId)
        }
    }
}

object CONSTANTS {
    const val HOME_SCREEN = "homeScreen"
    const val ADD_SCREEN = "addScreen"
    const val DETAIL_SCREEN = "detailScreen"
}