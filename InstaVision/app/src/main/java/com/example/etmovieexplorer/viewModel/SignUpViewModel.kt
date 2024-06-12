package com.example.etmovieexplorer.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.etmovieexplorer.preferences.PrefManager

class SignUpViewModel(context: Context) : ViewModel() {

    private val prefManager = PrefManager(context)

    fun signUp(
        name: String,
        email: String,
        phoneNo: String,
        password: String,
        confirmPassword: String,
        onSignUpSuccess: () -> Unit,
        onSignUpError: (String) -> Unit
    ) {
        if (name.isEmpty() || email.isEmpty() || phoneNo.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            onSignUpError("Please fill in all details.")
            return
        }

        if (password != confirmPassword) {
            onSignUpError("Passwords do not match.")
            return
        }

        if (prefManager.checkAlreadyExist(email)) {
            onSignUpError("User already exists with this email.")
            return
        }

        prefManager.saveUserDetail(email, password)
        prefManager.setLoginStatus(true)
        onSignUpSuccess()
    }

    fun loginUser(email: String, password: String): Boolean {
        val userDetail = prefManager.getUserDetails()
        return userDetail != null && userDetail.first == email && userDetail.second == password
    }
}