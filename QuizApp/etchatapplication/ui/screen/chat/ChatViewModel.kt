package com.example.etchatapplication.ui.screen.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _textMessage = MutableStateFlow("")
    val textMessage: StateFlow<String> = _textMessage

    private val _chatRoomId = MutableStateFlow<String?>(null)
    val chatRoomId: StateFlow<String?> = _chatRoomId

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun onTextMessageChange(message: String) {
        _textMessage.value = message
    }

    fun getOrCreateChatRoom(participants: List<String>) {
        firestoreRepository.getOrCreateChatRoom(participants) { chatRoomId ->
            _chatRoomId.value = chatRoomId
            if (chatRoomId != null) {
                observeMessages(chatRoomId)
            } else {
                Log.e("ChatViewModel", "Failed to create or get chat room")
            }
        }
    }

    fun sendMessage() {
        val chatRoomId = _chatRoomId.value ?: return
        val messageText = _textMessage.value.trim()
        if (messageText.isNotEmpty()) {
            firestoreRepository.sendMessage(chatRoomId, messageText) { success ->
                if (success) {
                    _textMessage.value = ""
                } else {
                    Log.e("ChatViewModel", "Failed to send message")
                }
            }
        }
    }

    private fun observeMessages(chatRoomId: String) {
        viewModelScope.launch {
            firestoreRepository.getMessages(chatRoomId).collect { messages ->
                _messages.value = messages
            }
        }
    }
}