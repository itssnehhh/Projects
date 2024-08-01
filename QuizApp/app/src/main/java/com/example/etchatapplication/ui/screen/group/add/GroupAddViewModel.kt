package com.example.etchatapplication.ui.screen.group.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestore.GroupRepository
import com.example.etchatapplication.repository.firestore.UserRepository
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
        getUserList()
    }

    private fun getUserList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _isLoading.value = true
                withContext(Dispatchers.IO) {
                    delay(1000)
                    try {
                        userRepository.getUsersList { users ->
                            _userList.value = users
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        _isLoading.value = false
                    }
                }
            }
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

