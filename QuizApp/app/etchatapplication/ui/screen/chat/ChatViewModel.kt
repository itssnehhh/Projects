package com.example.etchatapplication.ui.screen.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val userList = MutableStateFlow<List<User>>(emptyList())

    private val _textMessage = MutableStateFlow("")
    val textMessage: StateFlow<String> = _textMessage

    private lateinit var receiverEmail: String

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun initView(receiverEmail: String) {
        this.receiverEmail = receiverEmail
        loadMessages()
    }

    init {
        getUserList()
    }

    fun onTextMessageChange(text: String) {
        _textMessage.value = text
    }

    private fun getUserList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firestoreRepository.getUsersList { users ->
                    userList.value = users
                }
            }
        }
    }

    fun sendMessage(textMessage: String) {
        if (textMessage.isNotBlank()) {
            viewModelScope.launch {
                firestoreRepository.sendMessage(receiverEmail, textMessage) { success ->
                    if (success) {
                        _textMessage.value = ""
                    }
                }
            }
        }
    }

    fun uploadImageAndSendMessage(imageUri: Uri, textMessage: String) {
        viewModelScope.launch {
            firestoreRepository.sendMessageWithImage(receiverEmail, textMessage, imageUri) { success ->
                if (success) {
                    _textMessage.value = ""
                }
            }
        }
    }

    private fun loadMessages() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email ?: return

        viewModelScope.launch {
            firestoreRepository.getMessages(senderEmail, receiverEmail).collect { message ->
                _messages.value = message
            }
        }
    }
}