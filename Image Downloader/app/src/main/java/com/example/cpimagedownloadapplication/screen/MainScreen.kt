package com.example.cpimagedownloadapplication.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cpimagedownloadapplication.R
import com.example.cpimagedownloadapplication.constants.Constants.Companion.HOME_SCREEN
import com.example.cpimagedownloadapplication.constants.Constants.Companion.IMAGE_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Surface(color = Color.White) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color(0xFFDF6DF3))
                )
            }, content = {
                NavHostContainer(navController = navController, padding = it)
            }
        )
    }
}

@Composable
fun NavHostContainer(navController: NavHostController, padding: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN,
        modifier = Modifier.padding(paddingValues = padding),
        builder = {
            composable(HOME_SCREEN) {
                HomeScreen(navController)
            }
            composable("$IMAGE_SCREEN/{id}") { navBackStack ->

                val string = navBackStack.arguments?.getString("id")

                ImageScreen(string!!)
            }
        })
}
