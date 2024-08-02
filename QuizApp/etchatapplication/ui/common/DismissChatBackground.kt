package com.example.etchatapplication.ui.common

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.etchatapplication.R
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.ui.screen.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackGround(
    dismissState: SwipeToDismissBoxState,
    currentChatRoom: ChatRoom,
    viewModel: HomeViewModel,
) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color.Red
        else -> Color.Transparent
    }
    val showDialog by viewModel.showDialog.collectAsState()

    val context = LocalContext.current
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, 8.dp)
            .background(color, CardDefaults.shape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (direction == SwipeToDismissBoxValue.EndToStart) {
            LaunchedEffect(Unit) {
                viewModel.onDialogStatusChange(true)
                dismissState.snapTo(SwipeToDismissBoxValue.EndToStart)
            }
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        if (showDialog) {
            ShowAlertDialog(
                onDismissRequest = { viewModel.onDialogStatusChange(false) },
                onConfirmClick = {
                    viewModel.deleteChatRoom(currentChatRoom.chatroomId)
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_chat_delete), Toast.LENGTH_SHORT
                    ).show()
                },
                confirmButtonText = stringResource(R.string.btn_yes),
                onDismissClick = { viewModel.onDialogStatusChange(false) },
                title = stringResource(R.string.delete_chat),
                text = stringResource(R.string.delete_chat_message)
            )
        }
    }
}

