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
class GroupScreenViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _groupList = MutableStateFlow<List<Group>>(emptyList())
    val groupList : StateFlow<List<Group>> = _groupList

    init {
        getGroupList()
    }

    private fun getGroupList(){
        viewModelScope.launch {
            try {
                firestoreRepository.getGroups { groupList ->
                    Log.d("GROUP_LIST", "getGroupList: $groupList ")
                    _groupList.value = groupList
                }
            }catch (e:Exception){
                Log.d("GROUP_LIST", "Error fetching groupList :- ${e.message}")
            }
        }
    }
}