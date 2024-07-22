package com.example.etchatapplication.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.etchatapplication.CONSTANTS.LOGIN_SCREEN
import com.example.etchatapplication.R

@Composable
fun SettingsScreen(
    navController: NavHostController,
    darkTheme: Boolean,
    darkThemeChange: () -> Unit
) {

    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    var notification by rememberSaveable { mutableStateOf(true) }
    val currentUser by settingsViewModel.currentUser.observeAsState()
    val showDialog by settingsViewModel.showDialog.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            ProfileCard(
                userName = currentUser?.displayName ?: "User Name",
                userEmail = currentUser?.email ?: "Email"
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Settings",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            AppSettings(image = R.drawable.key, title = "Account")
            AppSettings(image = R.drawable.chat, title = "Chats")
            AppSettings(image = R.drawable.payment, title = "Payment")
            HorizontalDivider()
            Switch(
                image = R.drawable.notification,
                title = "Notification",
                checked = notification
            ) {
                notification = !notification
            }
            HorizontalDivider()
            Switch(image = R.drawable.night_mode, title = "Night Mode", checked = darkTheme) {
                darkThemeChange()
            }
            HorizontalDivider()
            AppSettings(
                image = R.drawable.logout,
                title = "Logout",
                modifier = Modifier.clickable {
                    settingsViewModel.onDialogStatusChange(true)

                }
            )
        }
    }
    if (showDialog) {
        AlertDialog(
            containerColor = Color(0xFF2BCA8D),
            onDismissRequest = { settingsViewModel.onDialogStatusChange(true) },
            confirmButton = {
                TextButton(
                    onClick = {
                        settingsViewModel.logOut()
                        navController.navigate(LOGIN_SCREEN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) {
                    Text(text = "Logout", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { settingsViewModel.onDialogStatusChange(false) }) {
                    Text(text = "Cancel", color = Color.White,style = MaterialTheme.typography.titleMedium)
                }
            },
            title = {
                Text(
                    text = "Logout",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "Are your sure you want to logout your account ?",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
    }
}

@Composable
fun AppSettings(image: Int, title: String, modifier: Modifier = Modifier) {
    HorizontalDivider()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(painter = painterResource(id = image), contentDescription = "")
        Text(
            text = title,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun Switch(image: Int, title: String, checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun ProfileCard(userName: String, userEmail: String) {
    Card(
        colors = CardDefaults.cardColors(Color(0xFF2BCA8D)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.account),
                contentDescription = "",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
                    .border(1.dp, Color.DarkGray, CircleShape)
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = userName,
                    modifier = Modifier.padding(4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = userEmail,
                    modifier = Modifier.padding(4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = NavHostController(LocalContext.current), false) { }
}