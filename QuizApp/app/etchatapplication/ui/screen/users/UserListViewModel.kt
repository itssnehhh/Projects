package com.example.etchatapplication.ui.screen.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.auth.FirebaseAuthRepository
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    init {
        getUserList()
    }

    fun getUserList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    firestoreRepository.getAllUsers { users ->
                        _userList.value = users
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}