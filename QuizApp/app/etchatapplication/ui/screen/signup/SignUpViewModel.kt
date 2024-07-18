package com.example.etchatapplication.ui.screen.signup

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.etchatapplication.repository.auth.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _cPassword = MutableStateFlow("")
    val cPassword: StateFlow<String> = _cPassword


    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _cPassword.value = confirmPassword
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
        return password.matches(passwordRegex.toRegex())
    }

    fun signUp(
        email: String,
        password: String,
        confirmPassword: String,
        context: Context,
        onResult: (Boolean) -> Unit,
    ) {

        when {
            email.isEmpty() || !isEmailValid(email) -> {
                Toast.makeText(context, "Please enter valid email address", Toast.LENGTH_SHORT)
                    .show()
                onResult(false)
            }

            password.isEmpty() || !isPasswordValid(password) -> {
                Toast.makeText(context, "Please enter valid password", Toast.LENGTH_SHORT).show()
                onResult(false)
            }

            confirmPassword.isEmpty() || confirmPassword != password -> {
                Toast.makeText(context, "Password mismatched", Toast.LENGTH_SHORT).show()
                onResult(false)
            }

            else -> {
                authRepository.signUp(email, password) { success ->
                    onResult(success)
                }
            }
        }
    }
}