package com.example.etchatapplication.ui.screen.group.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _newMessage = MutableStateFlow("")
    val newMessage: StateFlow<String> = _newMessage

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private lateinit var groupId: String

    fun init(groupId: String) {
        this.groupId = groupId
        loadMessages(groupId)
    }

    private fun loadMessages(groupId: String) {
        firestoreRepository.getGroupMessages(groupId) { messages ->
            _messages.value = messages
        }
    }

    fun sendMessage(groupId: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.email ?: ""
        val content = _newMessage.value.trim()
        if (content.isNotBlank()) {
            firestoreRepository.sendMessageToGroup(groupId, senderId, content, null)
            _newMessage.value = ""
        }
    }

    fun uploadImageAndSendMessage(uri: Uri) {
        firestoreRepository.uploadImage(uri) { imageUrl ->
            sendImageMessage(groupId, imageUrl)
        }
    }

    private fun sendImageMessage(groupId: String, imageUrl: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.email ?: ""
        firestoreRepository.sendMessageToGroup(groupId, senderId, "", imageUrl)
    }

    fun onImageUriChange(uri: Uri?) {
        _imageUri.value = uri
    }

    fun updateMessage(message: String) {
        _newMessage.value = message
    }
}
