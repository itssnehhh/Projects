package com.example.contactkeeper.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.contactkeeper.R
import com.example.contactkeeper.data.model.Contact
import com.example.contactkeeper.ui.screen.CONSTANTS.ADD_SCREEN
import com.example.contactkeeper.ui.screen.home.HomeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackGround(
    dismissState: SwipeToDismissBoxState,
    currentItem: Contact,
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color.Green
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
            .background(color, CardDefaults.shape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == SwipeToDismissBoxValue.StartToEnd) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "",
                modifier = Modifier.padding(start = 16.dp)
            )
            LaunchedEffect(Unit) {
                delay(500)
                navController.navigate("$ADD_SCREEN/${currentItem.id}")
            }
        }
        Spacer(modifier = Modifier)
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
                onDismissRequest = { showDialog = true },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteImageFromFirebase(currentItem.profilePicture)
                            viewModel.deleteContact(currentItem.id)
                            showDialog = false
                            Toast.makeText(
                                context,
                                context.getString(R.string.item_removed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text(text = stringResource(R.string.delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                },
                title = { Text(text = stringResource(R.string.delete_contact)) },
                text = { Text(text = stringResource(R.string.delete_detail_message)) }
            )
        }
    }
}