package com.example.chatapplication.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.model.ChatRoom
import com.example.chatapplication.model.User
import com.example.chatapplication.repository.firestore.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
                _chatRoomList.value = chatRooms
                chatRooms.forEach { chatRoom ->
                    val otherUserId = chatRoom.participants.first { it != FirebaseAuth.getInstance().currentUser?.uid }
                    fetchUserDetails(otherUserId)
                }
            }
        }
    }

    private fun fetchUserDetails(userId: String) {
        firestoreRepository.getUserDetails(userId) { user ->
            _userDetails.update { it + (userId to user) }
        }
    }


    private val _unreadMessageCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val unreadMessageCounts: StateFlow<Map<String, Int>> = _unreadMessageCounts

    private fun fetchUnreadMessageCount(chatRoomId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestoreRepository.getUnreadMessageCount(chatRoomId, userId) { unreadCount ->
            _unreadMessageCounts.update { it + (chatRoomId to unreadCount) }
        }
    }
}