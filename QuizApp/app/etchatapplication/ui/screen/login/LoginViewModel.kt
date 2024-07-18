package com.example.etchatapplication.ui.screen.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }


    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun signIn(email: String, password: String, context: Context, onResult: (Boolean) -> Unit) {
        when {
            email.isEmpty() || !isEmailValid(email) -> {
                Toast.makeText(context, "Please enter valid email address", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
            password.isEmpty() -> {
                Toast.makeText(context, "Incorrect Password", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
            else -> {
                _isLoading.value = true
                authRepository.signIn(email,password) { success ->
                    _isLoading.value = false
                    onResult(success)
                }
            }

        }
    }
}