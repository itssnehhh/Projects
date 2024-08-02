package com.example.etchatapplication.ui.screen.group.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.repository.firestore.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupListScreenViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _groupList = MutableStateFlow<List<Group>>(emptyList())
    val groupList: StateFlow<List<Group>> = _groupList

    init {
        loadGroupList()
    }

    private suspend fun fetchGroupList() {
        try {
            val groupList = groupRepository.getGroups()
            Log.d("GROUP_LIST", "fetchGroupList: $groupList")
            groupList.forEach { group ->
                fetchUnreadMessageCount(group.id)
            }
            _groupList.value = groupList.sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            Log.d("GROUP_LIST", "Error fetching groupList: ${e.message}")
        }
    }

    private fun loadGroupList() {
        viewModelScope.launch {
            fetchGroupList()
        }
    }

    private suspend fun fetchUnreadMessageCount(groupId: String) {
        try {
            val unreadCount = groupRepository.getUnreadMessageCount(groupId)
            _groupList.update { groupList ->
                groupList.map { group ->
                    if (group.id == groupId) {
                        group.copy(unreadCount = unreadCount)
                    } else {
                        group
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Failed to fetch unread message count", e)
        }
    }

    fun markMessagesAsRead(groupId: String) {
        viewModelScope.launch {
            try {
                val success = groupRepository.markMessagesAsRead(groupId)
                if (success) {
                    fetchUnreadMessageCount(groupId)
                } else {
                    Log.e("HomeViewModel", "Failed to mark messages as read in $groupId")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to mark messages as read", e)
            }
        }
    }
}