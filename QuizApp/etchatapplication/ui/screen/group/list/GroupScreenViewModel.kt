package com.example.etchatapplication.ui.screen.group.list

import android.util.Log
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
class GroupViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups

    init {
        getGroups()
    }

    private fun getGroups() {
        viewModelScope.launch {
            try {
                firestoreRepository.getGroups { groupList ->
                    Log.d("GroupViewModel", "Fetched groups: $groupList")
                    _groups.value = groupList
                }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Error fetching groups: ${e.message}", e)
            }
        }
    }
}
