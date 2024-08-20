package com.example.firebaseauth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPTextFields(
    modifier: Modifier = Modifier,
    length: Int,
    onFilled: (code: String) -> Unit,
) {
    var code by remember { mutableStateOf(CharArray(length)) }
    val focusRequesters = List(length) { FocusRequester() }

    Row(
        modifier = modifier.height(50.dp)
    ) {
        (0 until length).forEach { index ->

            OutlinedTextField(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .focusRequester(focusRequesters[index]),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                singleLine = true,
                value = code[index].takeIf { it.isDigit() }?.toString() ?: "",
                onValueChange = { value: String ->
                    if (value.isNotEmpty()) {
                        code[index] = value.first()
                        if (index < length - 1) {
                            focusRequesters[index + 1].requestFocus()
                        } else {
                            onFilled(code.concatToString())
                        }
                    } else {
                        code[index] = ' '
                        if (index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index < length - 1) ImeAction.Next else ImeAction.Done
                )
            )

            if (index < length - 1) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

