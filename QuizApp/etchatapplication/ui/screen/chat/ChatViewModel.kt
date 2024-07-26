package com.example.etchatapplication.ui.screen.chat

import android.net.Uri
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

    private val _image = MutableStateFlow(Uri.parse(""))
    val image: StateFlow<Uri?> = _image

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun onTextMessageChange(text: String) {
        _textMessage.value = text
    }

    fun onImageUrlChange(imageUrl: Uri?) {
        _image.value = imageUrl
    }

    fun getOrCreateRoom(receiverId: String) {
        firestoreRepository.getOrCreateChatRoom(receiverId) { chatRoomId ->
            _chatRoomId.value = chatRoomId
            if (chatRoomId != null) {
                observeMessage(chatRoomId)
            } else {
                Log.e("ChatViewModel", "Failed to get or create chat room")
            }
        }
    }

    fun sendMessage(message: String) {
        val chatRoomId = _chatRoomId.value ?: return
        val messageText = message.trim()
        if (messageText.isNotBlank()) {
            firestoreRepository.sendMessageToRoom(
                chatRoomId,
                messageText,
                _image.value.toString()
            ) { success ->
                if (success) {
                    _textMessage.value = ""
                    _image.value = null
                } else {
                    Log.e("ChatViewModel", "Failed to send message")
                }
            }
        }
    }

    fun uploadImageAndSendMessage(imageUri: Uri, textMessage: String) {
        val chatRoomId = _chatRoomId.value ?: return
        viewModelScope.launch {
            firestoreRepository.sendMessageWithImage(
                chatRoomId,
                textMessage,
                imageUri
            ) { success ->
                if (success) {
                    _textMessage.value = ""
                    _image.value = Uri.parse("")
                }
            }
        }
    }

    private fun observeMessage(chatRoomId: String) {
        viewModelScope.launch {
            firestoreRepository.getMessageFromChatRoom(chatRoomId).collect { messages ->
                _messages.value = messages
                println(messages)
            }
        }
    }
}