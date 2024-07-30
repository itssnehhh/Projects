package com.example.chatapplication.ui.screen.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.repository.auth.FirebaseAuthRepository
import com.example.chatapplication.repository.firestore.FirestoreRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository
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

    private fun fetchUserImageUrl(userId: String) {
        firestoreRepository.getUserDetails(userId) { user ->
            _userImageUrl.value = user.image
        }
    }

    fun onDialogStatusChange(status: Boolean) {
        _showDialog.value = status
    }

    fun logOut() {
        authRepository.logOut()
        _currentUser.value = null
    }
}