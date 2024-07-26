package com.example.etchatapplication.ui.screen.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.repository.auth.FirebaseAuthRepository
import com.example.etchatapplication.repository.firestorage.StorageRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _profileImageUrl = MutableLiveData<String?>()
    val profileImageUrl: LiveData<String?> = _profileImageUrl

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    init {
        _currentUser.value = authRepository.getCurrentUser()
    }

    fun onDialogStatusChange(status: Boolean) {
        _showDialog.value = status
    }

    fun logOut() {
        authRepository.logOut()
        _currentUser.value = null
        _profileImageUrl.value = null
    }
}
