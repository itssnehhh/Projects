package com.example.etchatapplication.ui.screen.chat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.model.User
import com.example.etchatapplication.repository.firestore.ChatRoomRepository
import com.example.etchatapplication.repository.firestore.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRoomRepository: ChatRoomRepository
) : ViewModel() {

    private val _textMessage = MutableStateFlow("")
    val textMessage: StateFlow<String> = _textMessage

    private val _chatRoomId = MutableStateFlow<String?>(null)

    private val _image = MutableStateFlow<Uri?>(Uri.parse(""))
    val image: StateFlow<Uri?> = _image

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _userDetails = MutableStateFlow<User?>(null)
    val userDetails: StateFlow<User?> = _userDetails

    private val _preview = MutableStateFlow(false)
    val preview: StateFlow<Boolean> = _preview

    fun onTextMessageChange(text: String) {
        _textMessage.value = text
    }

    fun onImageUrlChange(imageUrl: Uri?) {
        _image.value = imageUrl
    }

    fun getOrCreateRoom(receiverId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatRoomRepository.getOrCreateChatRoom(receiverId) { chatRoomId ->
                    Log.d("ChatViewModel", "Chat room ID: $chatRoomId")
                    if (chatRoomId != null) {
                        _chatRoomId.value = chatRoomId
                        observeMessage(chatRoomId)
                    } else {
                        Log.e("ChatViewModel", "Failed to get or create chat room")
                    }
                }
            }
        }
    }

    fun sendMessage(message: String) {
        val chatRoomId = _chatRoomId.value ?: return
        val messageText = message.trim()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (messageText.isNotBlank()) {
                    chatRoomRepository.sendMessageToRoom(
                        chatRoomId,
                        messageText,
                        _image.value.toString()
                    ) { success ->
                        if (success) {
                            _textMessage.value = ""
                            _image.value = Uri.parse("")
                        } else {
                            Log.e("ChatViewModel", "Failed to send message")
                        }
                    }
                }
            }
        }
    }

    fun uploadImageAndSendMessage(imageUri: Uri, textMessage: String) {
        val chatRoomId = _chatRoomId.value ?: return
        viewModelScope.launch {
            _preview.value = true
            withContext(Dispatchers.IO) {
                try {
                    chatRoomRepository.sendMessageWithImage(
                        chatRoomId,
                        textMessage,
                        imageUri
                    ) { success ->
                        if (success) {
                            _textMessage.value = ""
                            _image.value = Uri.parse("")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _preview.value = false
                }
            }
        }
    }

    private fun observeMessage(chatRoomId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatRoomRepository.getMessageFromChatRoom(chatRoomId).collect { messages ->
                    _messages.value = messages
                    println(messages)
                }
            }
        }
    }

    fun getUserDetails(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.getUserDetails(userId) { user ->
                    _userDetails.value = user
                }
            }
        }
    }
}
