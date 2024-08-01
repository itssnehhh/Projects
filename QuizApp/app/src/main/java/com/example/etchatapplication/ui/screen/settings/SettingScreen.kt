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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.etchatapplication.R
import com.example.etchatapplication.constants.CONSTANTS.LOGIN_SCREEN
import com.example.etchatapplication.ui.common.ShowAlertDialog
import com.example.etchatapplication.ui.theme.CustomGreen

@Composable
fun SettingsScreen(
    navController: NavHostController,
    darkTheme: Boolean,
    darkThemeChange: () -> Unit,
) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    var notification by rememberSaveable { mutableStateOf(true) }
    val currentUser by settingsViewModel.currentUser.observeAsState()
    val showDialog by settingsViewModel.showDialog.collectAsState()
    val userImageUrl by settingsViewModel.userImageUrl.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            ProfileCard(
                userName = currentUser?.displayName ?: stringResource(R.string.user_name),
                userEmail = currentUser?.email ?: stringResource(R.string.email),
                userImageUrl = userImageUrl
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.settings),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            AppSettings(image = R.drawable.key, title = stringResource(R.string.account))
            AppSettings(image = R.drawable.chat, title = stringResource(R.string.chats))
            AppSettings(image = R.drawable.payment, title = stringResource(R.string.payment))
            HorizontalDivider()
            Switch(
                image = R.drawable.notification,
                title = stringResource(R.string.notification),
                checked = notification
            ) {
                notification = !notification
            }
            HorizontalDivider()
            Switch(
                image = R.drawable.night_mode,
                title = stringResource(R.string.night_mode),
                checked = darkTheme
            ) {
                darkThemeChange()
            }
            HorizontalDivider()
            AppSettings(
                image = R.drawable.logout,
                title = stringResource(R.string.btn_logout),
                modifier = Modifier.clickable {
                    settingsViewModel.onDialogStatusChange(true)
                }
            )
        }
    }
    if (showDialog) {
        ShowAlertDialog(
            onDismissRequest = { settingsViewModel.onDialogStatusChange(false) },
            onConfirmClick = {
                settingsViewModel.logOut()
                navController.navigate(LOGIN_SCREEN) {
                    popUpTo(0) { inclusive = true }
                }
            },
            confirmButtonText = stringResource(id = R.string.btn_logout),
            onDismissClick = { settingsViewModel.onDialogStatusChange(false) },
            title = stringResource(R.string.logout),
            text = stringResource(R.string.logout_dialog_msg)
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
            colors = SwitchDefaults.colors(
                checkedTrackColor = CustomGreen,
                uncheckedTrackColor = Color.LightGray,
                checkedThumbColor = Color.White
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun ProfileCard(userName: String, userEmail: String, userImageUrl: String?) {
    Card(
        colors = CardDefaults.cardColors(CustomGreen),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row {
            if (userImageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(
                        userImageUrl,
                        placeholder = painterResource(R.drawable.account)
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.DarkGray, CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = "",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp)
                        .border(1.dp, Color.DarkGray, CircleShape)
                )
            }
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
