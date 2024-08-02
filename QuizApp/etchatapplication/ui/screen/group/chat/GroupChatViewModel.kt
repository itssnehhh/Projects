package com.example.etchatapplication.ui.screen.group.chat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.repository.firestore.GroupRepository
import com.example.etchatapplication.repository.firestore.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList
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
            try {
                groupRepository.getGroupMessages(groupId).collect { messages ->
                    fetchUserNames(messages)
                    _messages.value = messages
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Failed to load messages", e)
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
            try {
                val group = groupRepository.getGroupDetails(groupId)
                if (group != null) {
                    _groupName.value = group.name
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Failed to load group details", e)
            }
        }
    }

    fun fetchUserNames(messages: List<Message>) {
        val userIds = messages.map { it.senderId }.distinct()
        val userMap = mutableMapOf<String, String>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val deferredUsers = userIds.map { userId ->
                    async {
                        try {
                            val user = userRepository.getUserDetails(userId)
                            "${user.firstname} ${user.lastname}"
                        } catch (e: Exception) {
                            Log.e("ChatViewModel", "Failed to fetch user details for $userId", e)
                            "Unknown User"
                        }
                    }
                }

                // Await all deferred results
                val results = deferredUsers.awaitAll()
                userIds.forEachIndexed { index, userId ->
                    userMap[userId] = results[index]
                }
            }

            withContext(Dispatchers.Main) {
                _userNameMap.value = userMap
            }

        }
    }

    fun uploadImageAndSendMessage(uri: Uri, groupId: String) {
        viewModelScope.launch {
            try {
                val imageUrl = groupRepository.uploadImage(uri)
                sendImageMessage(groupId, imageUrl)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Failed to upload image and send message", e)
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








