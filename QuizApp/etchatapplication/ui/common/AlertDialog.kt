package com.example.etchatapplication.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.etchatapplication.R
import com.example.etchatapplication.ui.theme.CustomGreen

@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    confirmButtonText: String,
    onDismissClick: () -> Unit,
    title: String,
    text: String
) {
    AlertDialog(
        containerColor = CustomGreen,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmClick
            ) {
                Text(
                    text = confirmButtonText,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissClick) {
                Text(
                    text = stringResource(R.string.btn_cancel),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        title =
        {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}