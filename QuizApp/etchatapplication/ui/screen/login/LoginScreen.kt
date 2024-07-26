package com.example.etchatapplication.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.etchatapplication.CONSTANTS.MAIN_SCREEN
import com.example.etchatapplication.CONSTANTS.SIGN_UP_SCREEN
import com.example.etchatapplication.R
import com.example.etchatapplication.ui.screen.signup.InputTextField

@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val passwordVisible by loginViewModel.passwordVisible.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )
        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            item {
                Text(
                    text = stringResource(id = R.string.talk_hub),
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                InputTextField(
                    value = email,
                    onValueChange = { loginViewModel.onEmailChange(it) },
                    label = { Text(text = stringResource(id = R.string.email_address)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = ""
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(
                        imeAction = ImeAction.Next
                    ),
                )
                InputTextField(
                    value = password,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    label = { Text(text = stringResource(id = R.string.password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = ""
                        )
                    },
                    trailingIcon = {
                        val image = if (passwordVisible) R.drawable.hidden else R.drawable.show
                        IconButton(onClick = { loginViewModel.onVisibilityChange(!passwordVisible) }) {
                            Image(painter = painterResource(id = image), "")
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(
                        imeAction = ImeAction.Next
                    ),
                )
                TextButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        textAlign = TextAlign.End,
                        color = Color(0xFF2BCA8D),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xFF2BCA8D)),
                    onClick = {
                        loginViewModel.checkCurrentUser(email, password, context) { isExist ->
                            if (isExist) {
                                navController.navigate(MAIN_SCREEN)
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.toast_login_fail), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = stringResource(id = R.string.btn_login))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(text = stringResource(R.string.or), modifier = Modifier.padding(8.dp))
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxSize()
                )

                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .border(1.dp, Color.LightGray, ButtonDefaults.shape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "",
                    )
                    Text(
                        text = stringResource(R.string.btn_google),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)

                    )
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(Color(0xFF3DA8FF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = ""
                    )
                    Text(
                        text = stringResource(R.string.btn_facebook),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxSize()
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.don_t_have_an_account),
                        fontWeight = FontWeight.W400,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    TextButton(onClick = { navController.navigate(SIGN_UP_SCREEN) }) {
                        Text(
                            text = stringResource(id = R.string.btn_signup),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2BCA8D),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        LoadingDialog(isLoading)
    }
}

@Composable
fun LoadingDialog(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(Color.Black.copy(alpha = 0.6f)),
                modifier = Modifier
                    .align(Alignment.Center),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2BCA8D))
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Loading...",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
