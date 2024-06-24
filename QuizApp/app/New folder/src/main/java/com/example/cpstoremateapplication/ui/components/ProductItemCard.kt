package com.example.cpstoremateapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.cpstoremateapplication.data.model.Product
import com.example.cpstoremateapplication.ui.screen.ProductCard
import com.example.cpstoremateapplication.viewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    product: Product,
    navController: NavHostController,
    viewModel: ProductViewModel,
) {
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(newValue = product)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                false
            } else false
        }, positionalThreshold = { fullWidth -> fullWidth * 0.2f  }
    )
    AnimatedVisibility(show, exit = fadeOut(spring())) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                DismissBackGround(
                    dismissState = dismissState,
                    currentItem = currentItem,
                    viewModel = viewModel
                )
            },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            content = {
                ProductCard(
                    product = product,
                    navController = navController,
                    productViewModel = viewModel
                )
            }
        )
    }
}