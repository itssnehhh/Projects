package com.example.etchatapplication.ui.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.etchatapplication.R
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.model.User
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackGround(
    dismissState: SwipeToDismissBoxState,
    currentChat: ChatRoom,
) {

    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color.Red
        else -> Color.Transparent
    }
    val context = LocalContext.current
    val direction = dismissState.dismissDirection
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, 8.dp)
            .background(color, RectangleShape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (direction == SwipeToDismissBoxValue.EndToStart) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                modifier = Modifier.padding(end = 16.dp)
            )
            LaunchedEffect(Unit) {
                delay(500)
                showDialog = !showDialog
            }
        }

        if (showDialog) {
            AlertDialog(
                containerColor = Color(0xFF2BCA8D),
                onDismissRequest = { showDialog = true },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            Toast.makeText(
                                context,
                                "Chat Deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.btn_delete),
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(
                            text = stringResource(R.string.btn_cancel),
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.delete_conversation),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.delete_conversation_message),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}