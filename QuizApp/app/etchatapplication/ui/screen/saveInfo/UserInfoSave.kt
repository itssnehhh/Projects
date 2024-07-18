package com.example.etchatapplication.ui.screen.saveInfo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.etchatapplication.CONSTANTS.MAIN_SCREEN
import com.example.etchatapplication.R

@Composable
fun UserInfoSaveScreen(navController: NavHostController, email: String) {
    Log.d("EMAIL", "UserInfoSaveScreen: $email")
    val infoSaveViewModel = hiltViewModel<UserInfoSaveViewModel>()
    val firstName by infoSaveViewModel.firstName.collectAsState()
    val lastName by infoSaveViewModel.lastName.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    text = "Talk Hub",
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .size(160.dp)
                )
                TextField(
                    value = firstName,
                    onValueChange = { infoSaveViewModel.onFirstNameChange(it) },
                    label = { Text(text = "First Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2F8F0),
                        unfocusedContainerColor = Color(0xFFE2F8F0),
                        unfocusedIndicatorColor = Color(0xFF008652),
                        focusedIndicatorColor = Color(0xFF008652),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                TextField(
                    value = lastName,
                    onValueChange = { infoSaveViewModel.onLastNameChange(it) },
                    label = { Text(text = "Last Name") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE2F8F0),
                        unfocusedContainerColor = Color(0xFFE2F8F0),
                        unfocusedIndicatorColor = Color(0xFF008652),
                        focusedIndicatorColor = Color(0xFF008652),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xFF2BCA8D)),
                    onClick = {
                        navController.navigate(MAIN_SCREEN)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Preview
@Composable
fun UserInfoSaveScreenPreview() {
    UserInfoSaveScreen(NavHostController(LocalContext.current), "email")
}