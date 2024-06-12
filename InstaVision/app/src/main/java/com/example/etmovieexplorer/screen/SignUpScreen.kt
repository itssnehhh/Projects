package com.example.etmovieexplorer.screen

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
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.constants.Constants.LOGIN_SCREEN
import com.example.etmovieexplorer.constants.Constants.MAIN_SCREEN
import com.example.etmovieexplorer.preferences.PrefManager
import com.example.etmovieexplorer.viewModel.SignUpViewModel

@Composable
fun SignUpScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { SignUpViewModel(context) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                .padding(16.dp)
        ) {
            item {
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        unfocusedContainerColor = Color.LightGray
                    ),
                    maxLines = 1,
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(
                            stringResource(R.string.name),
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
                    maxLines = 1,
                    value = phoneNo,
                    onValueChange = { phoneNo = it },
                    label = {
                        Text(
                            stringResource(R.string.phoneNumber),
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
                    maxLines = 1,
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            stringResource(R.string.emailAddress),
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
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = {
                        Text(
                            stringResource(R.string.confirmPassword),
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                Button(
                    onClick = {
                        viewModel.signUp(
                            name = name,
                            email = email,
                            phoneNo = phoneNo,
                            password = password,
                            confirmPassword = confirmPassword,
                            onSignUpSuccess = {
                                navController.popBackStack()
                                navController.navigate(MAIN_SCREEN)
                            },
                            onSignUpError = {
                                errorMessage = it
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text(
                        text = stringResource(R.string.btn_signUp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth()
                    )
                }

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
                        text = stringResource(R.string.already_have_account),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    TextButton(onClick = { navController.navigate(LOGIN_SCREEN) }) {
                        Text(
                            color = Color.Red,
                            text = stringResource(R.string.btn_login),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = NavHostController(LocalContext.current))
}