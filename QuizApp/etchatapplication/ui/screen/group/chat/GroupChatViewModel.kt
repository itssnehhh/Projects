package com.example.etchatapplication.ui.screen.group.chat

import androidx.lifecycle.ViewModel
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _newMessage = MutableStateFlow("")
    val newMessage: StateFlow<String> = _newMessage

    fun loadMessages(groupId: String) {
        firestoreRepository.getGroupMessages(groupId) { messages ->
            _messages.value = messages
        }
    }

    fun sendMessage(groupId: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val content = _newMessage.value
        if (content.isNotBlank()) {
            firestoreRepository.sendMessageToGroup(groupId, senderId, content)
            _newMessage.value = ""
        }
    }

    fun updateMessage(message: String) {
        _newMessage.value = message
    }
}