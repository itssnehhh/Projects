package com.example.etmovieexplorer.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.constants.Constants.Companion.FAVOURITE_SCREEN
import com.example.etmovieexplorer.constants.Constants.Companion.IMAGE_SCREEN
import com.example.etmovieexplorer.constants.Constants.Companion.MOVIE_SCREEN
import com.example.etmovieexplorer.model.BottomNavItem
import com.example.etmovieexplorer.preferences.PrefManager
import com.example.etmovieexplorer.screen.Constants.LOGIN
import com.example.etmovieexplorer.ui.theme.CustomRed

@Composable
fun HomeScreen(navHostController: NavHostController, darkTheme: Boolean, function: () -> Unit) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val prefManager = remember { PrefManager(context) }

    Surface {
        Scaffold(
            topBar = { HomeTopAppBar(darkTheme, function, prefManager, navHostController) },
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }) { paddingValues ->
            NavHostContainer(navController = navController, padding = paddingValues)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    darkTheme: Boolean,
    onThemeChange: () -> Unit,
    prefManager: PrefManager,
    navController: NavHostController,
) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.movie_explorer),
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Switch(
                colors = SwitchDefaults.colors(
                    checkedBorderColor = Color.Black,
                    checkedTrackColor = CustomRed,
                    uncheckedTrackColor = Color.LightGray
                ),
                checked = darkTheme,
                onCheckedChange = { onThemeChange() }
            )
            Image(
                painter = painterResource(id = R.drawable.logout),
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        prefManager.setLoginStatus(false)
                        navController.navigate(LOGIN) {
                            popUpTo(0)
                        }
                        Toast
                            .makeText(
                                context,
                                context.getString(R.string.toast_logout),
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red
        )
    )
}

@Composable
fun NavHostContainer(navController: NavHostController, padding: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = MOVIE_SCREEN,
        modifier = Modifier.padding(padding),
        builder = {
            composable(MOVIE_SCREEN) {
                MovieListScreen(navController)
            }
            composable(FAVOURITE_SCREEN) {
                FavouriteScreen(navController)
            }
            composable(
                "movieDetails/{imdbID}",
                arguments = listOf(navArgument("imdbID") { type = NavType.StringType })
            ) { backStackEntry ->
                val imdbID = backStackEntry.arguments?.getString("imdbID") ?: ""
                MovieDetailScreen(imdbID = imdbID,navController)
            }
            composable("$IMAGE_SCREEN/{id}") { navBackStack ->
                val string = navBackStack.arguments?.getString("id")
                ImageScreen(string!!)
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavItem(
            label = stringResource(id = R.string.home),
            icon = Icons.Filled.Home,
            route = MOVIE_SCREEN
        ),
        BottomNavItem(
            label = stringResource(id = R.string.favourite),
            icon = Icons.Outlined.Favorite,
            route = FAVOURITE_SCREEN
        )
    )

    BottomAppBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = { navController.navigate(navItem.route) },
                icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.label) },
                label = { Text(text = navItem.label) },
                alwaysShowLabel = false
            )
        }
    }
}