package com.example.firebaseauth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onClick: (mobileNum: String, otp: String) -> Unit,
) {
    val context = LocalContext.current
    var otp: String? = null
    val phoneNumber = remember {
        mutableStateOf("")
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFF9FD0F7))
        ) {
            item {

                Text(text = "OTP SCREEN", color = Color.DarkGray)

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = phoneNumber.value,
                    onValueChange = { phoneNumber.value = it },
                    label = { Text(text = "Enter phone no.") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    onClick(phoneNumber.value, "")
                }, modifier = Modifier.fillMaxWidth(0.8f)) {
                    Text(text = "Send Otp")
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(text = "Enter the Otp")

                OTPTextFields(length = 6) { getOtp ->
                    otp = getOtp
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (otp != null) {
                            onClick("", otp!!)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    Text(
                        text = "Otp Verify",
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}