package com.example.etchatapplication.ui.screen.signup

import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.R
import com.example.etchatapplication.repository.auth.FirebaseAuthRepository
import com.example.etchatapplication.repository.storage.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val storageRepository: StorageRepository,
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

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onProfileImageUriChange(uri: Uri) {
        _profileImageUri.value = uri
    }

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
        imageUri: String?,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String,
        context: Context,
        onResult: (Boolean) -> Unit,
    ) {
        when {
            imageUri?.isEmpty() == true -> {
                Toast.makeText(context,
                    context.getString(R.string.toast_image_select), Toast.LENGTH_SHORT).show()   
            }
            firstName.isEmpty() || lastName.isEmpty() -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT
                ).show()
            }

            email.isEmpty() || !isEmailValid(email) -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_email_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
                onResult(false)
            }

            password.isEmpty() || !isPasswordValid(password) -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_valid_password), Toast.LENGTH_SHORT
                ).show()
                onResult(false)
            }

            confirmPassword.isEmpty() || confirmPassword != password -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_password_mismatch), Toast.LENGTH_SHORT
                ).show()
                onResult(false)
            }

            else -> {
                viewModelScope.launch {
                    _isLoading.value = true
                    withContext(Dispatchers.IO) {
                        authRepository.signUp(
                            fName = firstName,
                            lName = lastName,
                            email = email,
                            password = password,
                            imageUrl = imageUri ?: ""
                        ) { success ->
                            _isLoading.value = false
                            onResult(success)
                        }
                    }
                }
            }
        }
    }

    fun uploadProfileImage(uri: Uri, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val downloadUrl = storageRepository.uploadProfilePicture(uri)
                    withContext(Dispatchers.Main) {
                        onResult(downloadUrl)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        onResult(null)
                    }
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}