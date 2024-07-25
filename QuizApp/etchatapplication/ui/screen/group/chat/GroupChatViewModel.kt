package com.example.etchatapplication.ui.screen.group.chat

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
class GroupChatViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _newMessage = MutableStateFlow("")
    val newMessage: StateFlow<String> = _newMessage

    // Group ID should be set before calling fetchMessages or sendMessage
    lateinit var groupId: String

    fun updateMessage(message: String) {
        _newMessage.value = message
    }

    fun sendMessage() {
        val message = _newMessage.value.trim()
        if (message.isNotEmpty() && this::groupId.isInitialized) {
//            firestoreRepository.sendMessageToGroup(groupId, message) { success ->
//                if (success) {
//                    _newMessage.value = ""
//                }
//            }
        }
    }

    fun fetchMessages() {
        if (this::groupId.isInitialized) {
            viewModelScope.launch {
                firestoreRepository.getGroupMessages(groupId).collect { messages ->
                    _messages.value = messages
                }
            }
        }
    }
}