package com.example.etchatapplication.ui.screen.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.repository.auth.FirebaseAuthRepository
import com.example.etchatapplication.repository.firestore.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _userImageUrl = MutableStateFlow<String?>(null)
    val userImageUrl: StateFlow<String?> = _userImageUrl

    init {
        _currentUser.value = authRepository.getCurrentUser()
        _currentUser.value?.let { user ->
            fetchUserImageUrl(user.uid)
        }
    }

    fun onDialogStatusChange(status: Boolean) {
        _showDialog.value = status
    }

    private fun fetchUserImageUrl(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val user = userRepository.getUserDetails(userId)
                    withContext(Dispatchers.Main) {
                        _userImageUrl.value = user.image
                    }
                } catch (e: Exception) {
                    Log.e("ChatViewModel", "Failed to fetch user image URL for $userId", e)
                    // Handle errors if needed, such as setting a default image or logging the error
                }
            }
        }
    }

    fun logOut() {
        authRepository.logout()
        _currentUser.value = null
    }
}
