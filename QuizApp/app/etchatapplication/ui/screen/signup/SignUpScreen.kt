package com.example.etchatapplication.ui.screen.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.etchatapplication.CONSTANTS.LOGIN_SCREEN
import com.example.etchatapplication.CONSTANTS.MAIN_SCREEN
import com.example.etchatapplication.R

@Composable
fun SignUpScreen(navController: NavHostController) {

    val context = LocalContext.current
    val signUpViewModel = hiltViewModel<SignUpViewModel>()
    val firstName by signUpViewModel.firstName.collectAsState()
    val lastName by signUpViewModel.lastName.collectAsState()
    val email by signUpViewModel.email.collectAsState()
    val password by signUpViewModel.password.collectAsState()
    val confirmPassword by signUpViewModel.cPassword.collectAsState()
    val passwordVisible by signUpViewModel.passwordVisible.collectAsState()

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
                    text = stringResource(R.string.talk_hub),
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
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
                    onValueChange = { signUpViewModel.onFirstNameChange(it) },
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
                    onValueChange = { signUpViewModel.onLastNameChange(it) },
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

                TextField(
                    value = email,
                    onValueChange = { signUpViewModel.onEmailChange(it) },
                    label = { Text(text = stringResource(R.string.email_address)) },
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = ""
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(
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
                TextField(
                    value = password,
                    onValueChange = { signUpViewModel.onPasswordChange(it) },
                    maxLines = 1,
                    label = { Text(text = stringResource(R.string.password)) },
                    trailingIcon = {
                        val image = if (passwordVisible) R.drawable.hidden else R.drawable.show
                        IconButton(onClick = { signUpViewModel.onVisibilityChange(!passwordVisible) }) {
                            Image(painter = painterResource(id = image), "")
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = ""
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(
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
                TextField(
                    value = confirmPassword,
                    onValueChange = { signUpViewModel.onConfirmPasswordChange(it) },
                    label = { Text(text = stringResource(R.string.confirm_password)) },
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = ""
                        )
                    },
                    trailingIcon = {
                        val image = if (passwordVisible) R.drawable.hidden else R.drawable.show
                        IconButton(onClick = { signUpViewModel.onVisibilityChange(!passwordVisible) }) {
                            Image(painter = painterResource(id = image), "")
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(
                        imeAction = ImeAction.Next
                    ),
                    supportingText = { Text(text = "Password must be at least 8 characters long and must contains one capital text,one number,one special character") },
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
                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xFF2BCA8D)),
                    onClick = {
                        signUpViewModel.createAccount(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword,
                            context = context
                        ) { isValid ->
                            if (isValid) {
                                navController.navigate(MAIN_SCREEN) {
                                    popUpTo(LOGIN_SCREEN) { inclusive = true }
                                }
                            }else{
                                Toast.makeText(context, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = stringResource(R.string.btn_signup))
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.already_have_an_account),
                        fontWeight = FontWeight.W400,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    TextButton(
                        onClick = { navController.navigate(LOGIN_SCREEN) }) {
                        Text(
                            text = stringResource(R.string.btn_login),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2BCA8D),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
