package com.example.cpstoremateapplication.ui.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cpstoremateapplication.ui.screen.Constants.DETAIL_SCREEN
import com.example.cpstoremateapplication.ui.screen.Constants.FAVOURITE_SCREEN
import com.example.cpstoremateapplication.ui.screen.Constants.HOME_SCREEN
import com.example.cpstoremateapplication.viewModel.ProductViewModel
import com.example.storemateproductapplication.ui.screen.FavouriteScreen

@Composable
fun MainScreen(
    productViewModel: ProductViewModel,
) {
    val navController = rememberNavController()
    NavHostContainer(navController, productViewModel)
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    productViewModel: ProductViewModel,
) {
    NavHost(navController = navController, startDestination = HOME_SCREEN) {
        composable(HOME_SCREEN) {
            HomeScreen(navController, productViewModel)
        }
        composable(
            "$DETAIL_SCREEN/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType }),
            enterTransition = {
                fadeIn(animationSpec = tween(500, easing = LinearEasing)) +
                        slideIntoContainer(
                            animationSpec = tween(500, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
            }, exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        500, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(500, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { navBackStackEntry ->
            val productId = navBackStackEntry.arguments?.getInt("productId")
            ProductDetailScreen(navController, productViewModel, productId)
        }
        composable(
            route = FAVOURITE_SCREEN,
            enterTransition = {
                fadeIn(animationSpec = tween(500, easing = LinearEasing)) +
                        slideIntoContainer(
                            animationSpec = tween(500, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        500, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(500, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            FavouriteScreen(navController,productViewModel)
        }
    }
}

object Constants {
    const val HOME_SCREEN = "homeScreen"
    const val DETAIL_SCREEN = "detailScreen"
    const val FAVOURITE_SCREEN = "favouriteScreen"
}
