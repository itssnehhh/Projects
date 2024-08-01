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
        getGroupList()
    }

    private fun getGroupList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    groupRepository.getGroups { groupList ->
                        Log.d("GROUP_LIST", "getGroupList: $groupList ")
                        _groupList.value = groupList.sortedByDescending { it.timestamp }
                        groupList.forEach { group: Group ->
                            fetchUnreadMessageCount(group.id)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("GROUP_LIST", "Error fetching groupList :- ${e.message}")
                }
            }
        }
    }

    private fun fetchUnreadMessageCount(groupId: String) {
        viewModelScope.launch {
            groupRepository.getUnreadMessageCount(groupId) { unreadCount ->
                _groupList.update { groupList ->
                    groupList.map { group ->
                        if (group.id == groupId) {
                            group.copy(unreadCount = unreadCount)
                        } else {
                            group
                        }
                    }
                }
            }
        }
    }

    fun markMessagesAsRead(groupId: String) {
        viewModelScope.launch {
            groupRepository.markMessagesAsRead(groupId) { success ->
                if (success) {
                    fetchUnreadMessageCount(groupId)
                } else {
                    Log.e("HomeViewModel", "Failed to mark messages as read in $groupId")
                }
            }
        }
    }
}