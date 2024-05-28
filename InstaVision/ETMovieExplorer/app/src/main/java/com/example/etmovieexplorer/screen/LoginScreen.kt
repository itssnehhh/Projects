package com.example.etmovieexplorer.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.preferences.PrefManager
import com.example.etmovieexplorer.screen.Constants.HOME
import com.example.etmovieexplorer.screen.Constants.SIGNUP

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
            alpha = 0.8f,
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                fontWeight = FontWeight.ExtraBold,
                color = Color.Red,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.headlineMedium,
                text = "Login Screen",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black, shape = CardDefaults.shape)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
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

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        val credentials = prefManager.getUserCredentials()
                        if (credentials != null && credentials.first == email && credentials.second == password) {
                            prefManager.setLoginStatus(true)
                            navController.popBackStack()
                            navController.navigate(HOME)
                        } else {
                            errorMessage = "Invalid email or password"
                        }
                    } else {
                        errorMessage = "Please enter both email and password"
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
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                TextButton(onClick = { navController.navigate(SIGNUP) }) {
                    Text(
                        color = Color.Red,
                        text = stringResource(R.string.btn_signUp),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavHostController(LocalContext.current))
}