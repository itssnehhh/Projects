package com.example.etchatapplication.ui.screen.group.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.repository.firestore.GroupRepository
import com.example.etchatapplication.repository.firestore.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _newMessage = MutableStateFlow("")
    val newMessage: StateFlow<String> = _newMessage

    private val _imageUri = MutableStateFlow<Uri?>(null)

    private lateinit var groupId: String
    private val _userNameMap = MutableStateFlow<Map<String, String>>(emptyMap())

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName

    fun init(groupId: String) {
        this.groupId = groupId
        loadMessages(groupId)
        loadGroupDetails(groupId)
    }

    fun onImageUriChange(uri: Uri?) {
        _imageUri.value = uri
    }

    fun updateMessage(message: String) {
        _newMessage.value = message
    }

    fun getSenderName(senderId: String): String {
        return _userNameMap.value[senderId] ?: senderId
    }

    private fun loadMessages(groupId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groupRepository.getGroupMessages(groupId) { messages ->
                    _messages.value = messages
                    fetchUserNames(messages)
                }
            }
        }
    }

    fun sendMessage(groupId: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val content = _newMessage.value.trim()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (content.isNotBlank()) {
                    groupRepository.sendMessageToGroup(groupId, senderId, content, null)
                    _newMessage.value = ""
                }
            }
        }
    }

    private fun loadGroupDetails(groupId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groupRepository.getGroupDetails(groupId) { group ->
                    if (group != null) {
                        _groupName.value = group.name
                    }
                }
            }
        }
    }

    private fun fetchUserNames(messages: List<Message>) {
        val userIds = messages.map { it.senderId }.distinct()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                userIds.forEach { userId ->
                    userRepository.getUserDetails(userId) { user ->
                        val updatedMap = _userNameMap.value.toMutableMap()
                        updatedMap[userId] = "${user.firstname} ${user.lastname}"
                        _userNameMap.value = updatedMap
                    }
                }
            }
        }
    }

    fun uploadImageAndSendMessage(uri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groupRepository.uploadImage(uri) { imageUrl ->
                    sendImageMessage(groupId, imageUrl)
                }
            }
        }
    }

    private fun sendImageMessage(groupId: String, imageUrl: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groupRepository.sendMessageToGroup(groupId, senderId, "", imageUrl)
            }
        }
    }
}








