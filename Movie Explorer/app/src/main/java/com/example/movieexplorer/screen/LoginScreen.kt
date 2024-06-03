package com.example.movieexplorer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movieexplorer.R
import com.example.movieexplorer.constants.Constants.HOME_SCREEN
import com.example.movieexplorer.constants.Constants.MAIN_SCREEN
import com.example.movieexplorer.constants.Constants.SIGNUP_SCREEN
import com.example.movieexplorer.preferences.PrefManager

@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current
    val prefManager = remember { PrefManager(context) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = "",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        LazyColumn(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp, top = 16.dp)
        ) {
            item {
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        unfocusedContainerColor = Color.LightGray
                    ),
                    maxLines = 1,
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            stringResource(R.string.email),
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        unfocusedContainerColor = Color.LightGray
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible) R.drawable.hidden else R.drawable.show
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Image(painter = painterResource(id = image), "")
                        }
                    },
                    maxLines = 1,
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            stringResource(R.string.password),
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                TextButton(onClick = { }) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(R.string.forgot_password),
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Right
                    )
                }

                Button(
                    onClick = {
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            val userDetail = prefManager.getUserDetails()
                            if (userDetail != null && userDetail.first == email && userDetail.second == password) {
                                prefManager.setLoginStatus(true)
                                navController.popBackStack()
                                navController.navigate(MAIN_SCREEN)
                            } else {
                                errorMessage = context.getString(R.string.error_invalid)
                            }
                        } else {
                            errorMessage = context.getString(R.string.empty_error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text(
                        text = stringResource(R.string.btn_login),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(
                        color = Color.Red,
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.don_t_have_an_account),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    TextButton(onClick = { navController.navigate(SIGNUP_SCREEN) }) {
                        Text(
                            color = Color.Red,
                            text = stringResource(R.string.btn_signUp),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavHostController(LocalContext.current))
}