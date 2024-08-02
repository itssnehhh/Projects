package com.example.etchatapplication.ui.common

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
import androidx.navigation.NavHostController
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.model.User
import com.example.etchatapplication.ui.screen.home.HomeViewModel
import com.example.etchatapplication.ui.screen.home.UserChatList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomItem(
    chatRoom: ChatRoom,
    navController: NavHostController,
    viewModel: HomeViewModel,
    user: User,
) {
    val show by remember { mutableStateOf(true) }
    val currentChatRoom by rememberUpdatedState(newValue = chatRoom)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { false },
        positionalThreshold = { fullWidth -> fullWidth * 0.2f }
    )
    AnimatedVisibility(show, exit = fadeOut(spring())) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                DismissBackGround(
                    dismissState = dismissState,
                    currentChatRoom = currentChatRoom,
                    viewModel = viewModel
                )
            },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            content = {
                UserChatList(
                    chatRoom = chatRoom,
                    user = user,
                    navController = navController,
                    homeViewModel = viewModel
                )
            }
        )
    }
}