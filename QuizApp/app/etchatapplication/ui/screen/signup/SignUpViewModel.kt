package com.example.etchatapplication.ui.screen.signup

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.repository.auth.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    private val fName = MutableStateFlow("")
    val firstName: StateFlow<String> = fName

    private val lName = MutableStateFlow("")
    val lastName: StateFlow<String> = lName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _cPassword = MutableStateFlow("")
    val cPassword: StateFlow<String> = _cPassword

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onFirstNameChange(name: String) {
        fName.value = name
    }

    fun onLastNameChange(surname: String) {
        lName.value = surname
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _cPassword.value = confirmPassword
    }

    fun onVisibilityChange(status: Boolean) {
        _passwordVisible.value = status
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
        return password.matches(passwordRegex.toRegex())
    }

    fun createAccount(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String,
        context: Context,
        onResult: (Boolean) -> Unit
    ) {
        when {
            firstName.isEmpty() || lastName.isEmpty() -> {
                Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
            }

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
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        _isLoading.value = true
                        delay(1000L)
                        authRepository.signUp(firstName, lastName, email, password) { success ->
                            _isLoading.value = false
                            onResult(success)
                        }
                    }
                }
            }
        }
    }
}