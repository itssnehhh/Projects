package com.example.etchatapplication.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestore.ChatRoomRepository
import com.example.etchatapplication.repository.firestore.UserRepository
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
    private val userRepository: UserRepository,
    private val chatRoomRepository: ChatRoomRepository
) : ViewModel() {

    private val _chatRoomList = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRoomList: StateFlow<List<ChatRoom>> = _chatRoomList

    private val _userDetails = MutableStateFlow<Map<String, User>>(emptyMap())
    val userDetails: StateFlow<Map<String, User>> = _userDetails

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    init {
        getRoomChatList()
    }

    fun onDialogStatusChange(status: Boolean) {
        _showDialog.value = status
    }

    private fun getRoomChatList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatRoomRepository.getRoomChats().collect { chatRooms ->
                    chatRooms.forEach { chatRoom ->
                        val otherUserId =
                            chatRoom.participants.first { it != FirebaseAuth.getInstance().currentUser?.uid }
                        fetchUserDetails(otherUserId)
                        fetchUnreadMessageCount(chatRoom.chatroomId)
                    }
                    _chatRoomList.value = chatRooms.sortedByDescending { it.timestamp }
                }
            }
        }
    }

    private suspend fun fetchUserDetails(userId: String) {
        viewModelScope.launch {
        try {
            val user = userRepository.getUserDetails(userId)
            _userDetails.update { it + (userId to user) }
        } catch (e: Exception) {
            Log.e("ChatViewModel", "Failed to fetch user details", e)
        }}
    }



    private fun fetchUnreadMessageCount(chatRoomId: String) {
        viewModelScope.launch {
            val unreadCount = try {
                chatRoomRepository.getUnreadMessageCount(chatRoomId)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching unread message count", e)
                0
            }

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

    fun markMessagesAsRead(chatRoomId: String) {
        viewModelScope.launch {
            val success = try {
                chatRoomRepository.markMessagesAsRead(chatRoomId)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error marking messages as read", e)
                false
            }

            if (success) {
                fetchUnreadMessageCount(chatRoomId)
            } else {
                Log.e("HomeViewModel", "Failed to mark messages as read in $chatRoomId")
            }
        }
    }

    fun deleteChatRoom(chatRoomId: String) {
        viewModelScope.launch {
            val success = try {
                chatRoomRepository.deleteChatRooms(chatRoomId)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error deleting chat room", e)
                false
            }

            if (success) {
                Log.d("HomeViewModel", "Chat Deleted Successfully")
            } else {
                Log.d("HomeViewModel", "Chat can't be deleted.")
            }
        }
    }
}