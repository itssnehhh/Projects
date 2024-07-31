package com.example.etchatapplication.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _chatRoomList = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRoomList: StateFlow<List<ChatRoom>> = _chatRoomList

    private val _userDetails = MutableStateFlow<Map<String, User>>(emptyMap())
    val userDetails: StateFlow<Map<String, User>> = _userDetails

    init {
        getChattedUserList()
    }

    private fun getChattedUserList() {
        viewModelScope.launch {
            firestoreRepository.getRoomChats().collect { chatRooms ->
                _chatRoomList.value = chatRooms.sortedByDescending { it.timestamp }
                chatRooms.forEach { chatRoom ->
                    val otherUserId = chatRoom.participants.first { it != FirebaseAuth.getInstance().currentUser?.uid }
                    fetchUserDetails(otherUserId)
                    fetchUnreadMessageCount(chatRoom.chatroomId)
                }
            }
        }
    }

    private fun fetchUserDetails(userId: String) {
        viewModelScope.launch {
            firestoreRepository.getUserDetails(userId) { user ->
                _userDetails.update { it + (userId to user) }
            }
        }
    }

    private fun fetchUnreadMessageCount(chatRoomId: String) {
        viewModelScope.launch {
            firestoreRepository.getUnreadMessageCount(chatRoomId) { unreadCount ->
                _chatRoomList.update { chatRooms ->
                    chatRooms.map { chatRoom ->
                        if (chatRoom.chatroomId == chatRoomId) {
                            chatRoom.copy(unreadCount = unreadCount)
                        } else {
                            chatRoom
                        }
                    }
                }
            }
        }
    }

    fun markMessagesAsRead(chatRoomId: String) {
        viewModelScope.launch {
            firestoreRepository.markMessagesAsRead(chatRoomId) { success ->
                if (success) {
                    fetchUnreadMessageCount(chatRoomId)
                } else {
                    Log.e("HomeViewModel", "Failed to mark messages as read in $chatRoomId")
                }
            }
        }
    }
}
