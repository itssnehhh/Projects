package com.example.etchatapplication.ui.screen.group.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestore.GroupRepository
import com.example.etchatapplication.repository.firestore.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupAddViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun onGroupNameChange(name: String) {
        _groupName.value = name
    }

    init {
        loadUserList()
    }

    private suspend fun fetchUserList() {
        _isLoading.value = true
        try {
            val users = userRepository.getUsersList()
            _userList.value = users
        } catch (e: Exception) {
            Log.e("ChatViewModel", "Failed to fetch user list", e)
            // Handle errors if needed, such as showing a message to the user
        } finally {
            _isLoading.value = false
        }
    }

    // Call this function from a coroutine context
    private fun loadUserList() {
        viewModelScope.launch {
            fetchUserList()
        }
    }

    fun createGroup(name: String, selectedUser: List<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _isLoading.value = true
                try {
                    groupRepository.createGroup(name, selectedUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}

