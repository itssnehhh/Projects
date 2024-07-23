package com.example.etchatapplication.ui.screen.group.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupAddViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val groupList = mutableListOf<String>()

    fun onGroupNameChange(name: String) {
        _groupName.value = name
    }

    init {
        getUserList()
    }

    private fun getUserList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreRepository.getUsersList { users ->
                    _userList.value = users
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createGroup(name: String, selectedUsers: List<String>, callback: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreRepository.createGroup(name, selectedUsers) { groupId ->
                    callback(groupId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}