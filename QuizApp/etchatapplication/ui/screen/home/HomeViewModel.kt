package com.example.etchatapplication.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _userList = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRoomList: StateFlow<List<ChatRoom>> = _userList

    init {
        getChattedUserList()
    }

    private fun getChattedUserList() {
        viewModelScope.launch {
            firestoreRepository.getRoomChats().collect{
                _userList.value = it
            }
        }
    }
}