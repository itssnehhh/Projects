package com.example.contactkeeper.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.contactkeeper.data.model.Contact
import com.example.contactkeeper.ui.screen.home.ContactCard
import com.example.contactkeeper.ui.screen.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactItem(
    contact: Contact,
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    val show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(newValue = contact)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { false },
        positionalThreshold = { fullWidth -> fullWidth * 0.2f }
    )
    AnimatedVisibility(show, exit = fadeOut(spring())) {
        SwipeToDismissBox(
            state = dismissState,
            modifier = Modifier,
            backgroundContent = {
                DismissBackGround(dismissState, currentItem, navController, viewModel)
            },
            content = {
                ContactCard(
                    navController = navController,
                    contact = contact,
                )
            }
        )
    }
}
