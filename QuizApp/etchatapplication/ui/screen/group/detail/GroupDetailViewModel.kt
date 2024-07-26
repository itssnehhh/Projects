package com.example.etchatapplication.ui.screen.group.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.repository.firestore.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _group = MutableStateFlow<Group?>(null)
    val group: StateFlow<Group?> = _group

    fun loadGroupDetails(groupId: String) {
        viewModelScope.launch {
            firestoreRepository.getGroupDetails(groupId) { group ->
                _group.value = group
            }
        }
    }

    fun exitGroup(groupId: String, userId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            firestoreRepository.exitGroup(groupId, userId, onComplete)
        }
    }
}
