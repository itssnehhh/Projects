package com.example.etchatapplication.ui.screen.signup

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import coil.compose.rememberAsyncImagePainter
import com.example.etchatapplication.R
import com.example.etchatapplication.constants.CONSTANTS.LOGIN_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.MAIN_SCREEN
import com.example.etchatapplication.ui.common.InputTextField
import com.example.etchatapplication.ui.common.LoadingDialog
import com.example.etchatapplication.ui.theme.CustomGreen

@Composable
fun SignUpScreen(navController: NavHostController) {

    val context = LocalContext.current
    val signUpViewModel = hiltViewModel<SignUpViewModel>()
    val profileImageUri by signUpViewModel.profileImageUri.collectAsState()
    val firstName by signUpViewModel.firstName.collectAsState()
    val lastName by signUpViewModel.lastName.collectAsState()
    val email by signUpViewModel.email.collectAsState()
    val password by signUpViewModel.password.collectAsState()
    val confirmPassword by signUpViewModel.cPassword.collectAsState()
    val passwordVisible by signUpViewModel.passwordVisible.collectAsState()
    val isLoading by signUpViewModel.isLoading.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                signUpViewModel.onProfileImageUriChange(uri)
            }
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    painter = if (profileImageUri != null) rememberAsyncImagePainter(profileImageUri) else painterResource(
                        id = R.drawable.account
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .size(160.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
                Text(
                    text = stringResource(R.string.select_image),
                    modifier = Modifier.padding(bottom = 4.dp),
                    color = Color.Gray
                )
                InputTextField(
                    value = firstName,
                    onValueChange = { signUpViewModel.onFirstNameChange(it) },
                    label = { Text(text = stringResource(R.string.first_name)) },
                )
                InputTextField(
                    value = lastName,
                    onValueChange = { signUpViewModel.onLastNameChange(it) },
                    label = { Text(text = stringResource(R.string.last_name)) },
                )
                InputTextField(
                    value = email,
                    onValueChange = { signUpViewModel.onEmailChange(it) },
                    label = { Text(text = stringResource(R.string.email_address)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        .copy(imeAction = ImeAction.Next),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "")
                    }
                )
                InputTextField(
                    value = password,
                    onValueChange = { signUpViewModel.onPasswordChange(it) },
                    label = { Text(text = stringResource(R.string.password)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        .copy(imeAction = ImeAction.Next),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "")
                    },
                    trailingIcon = {
                        val image = if (passwordVisible) R.drawable.hidden else R.drawable.show
                        IconButton(onClick = { signUpViewModel.onVisibilityChange(!passwordVisible) }) {
                            Image(painter = painterResource(id = image), "")
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )
                InputTextField(
                    value = confirmPassword,
                    onValueChange = { signUpViewModel.onConfirmPasswordChange(it) },
                    label = { Text(text = stringResource(R.string.confirm_password)) },
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
                    supportingText = {
                        Text(
                            text = stringResource(R.string.password_hint),
                            color = Color.DarkGray
                        )
                    },
                )
                Button(
                    colors = ButtonDefaults.buttonColors(CustomGreen),
                    onClick = {
                        signUpViewModel.uploadProfileImage(
                            profileImageUri ?: Uri.EMPTY
                        ) { downloadUrl ->
                            signUpViewModel.createAccount(
                                imageUri = downloadUrl,
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
                                } else {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.toast_email_exist),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
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
                        onClick = {
                            navController.navigate(LOGIN_SCREEN)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.btn_login),
                            fontWeight = FontWeight.Bold,
                            color = CustomGreen,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        LoadingDialog(isLoading = isLoading)
    }
}

