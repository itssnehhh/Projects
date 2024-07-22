package com.example.etchatapplication.ui.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.ChatMessage
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: FirestoreRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText

    private lateinit var receiverEmail: String

    fun initView(receiverEmail: String) {
        this.receiverEmail = receiverEmail
        loadMessages()
    }

    fun onMessageChange(newText: String) {
        _messageText.value = newText
    }

    fun sendMessage() {
        val message = _messageText.value
        if (message.isNotBlank()) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val senderEmail = currentUser?.email ?: return

            viewModelScope.launch {
                repository.sendMessage(receiverEmail, message) { success ->
                    if (success) {
                        _messageText.value = ""
                        loadMessages()
                    }
                }
            }
        }
    }

    private fun loadMessages() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email ?: return

        viewModelScope.launch {
            repository.getMessages(senderEmail, receiverEmail).collect { messages ->
                _messages.value = messages
            }
        }
    }
}