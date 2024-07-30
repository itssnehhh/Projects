package com.example.chatapplication.ui.screen.group.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.model.Group
import com.example.chatapplication.repository.firestore.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupListScreenViewModel  @Inject constructor(
    private val firestoreRepository: FirestoreRepository
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
                    firestoreRepository.getGroups { groupList ->
                        Log.d("GROUP_LIST", "getGroupList: $groupList ")
                        _groupList.value = groupList
                    }
                } catch (e: Exception) {
                    Log.d("GROUP_LIST", "Error fetching groupList :- ${e.message}")
                }
            }
        }
    }
}