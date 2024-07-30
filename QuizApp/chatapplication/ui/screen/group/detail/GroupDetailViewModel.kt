package com.example.chatapplication.ui.screen.group.detail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.chatapplication.constants.CONSTANTS.GROUP_SCREEN
import com.example.chatapplication.model.Group
import com.example.chatapplication.model.User
import com.example.chatapplication.repository.firestore.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {

    private val _group = MutableStateFlow<Group?>(null)
    val group: StateFlow<Group?> = _group

    private val _show = MutableStateFlow(false)
    val show: StateFlow<Boolean> = _show

    private val _userDetails = MutableStateFlow<Map<String, User>>(emptyMap())
    val userDetails: StateFlow<Map<String, User>> = _userDetails

    fun showExitDialog(status: Boolean) {
        _show.value = status
    }

    fun loadGroupDetails(groupId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firestoreRepository.getGroupDetails(groupId) { group ->
                    _group.value = group

                    group?.users?.forEach { userId ->
                        if (userId != null) {
                            firestoreRepository.getUserDetails(userId) { user ->
                                // Update user details map
                                val updatedMap = _userDetails.value.toMutableMap()
                                updatedMap[user.id] = user
                                _userDetails.value = updatedMap
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteGroup(groupId: String, context: Context, innerNavController: NavHostController) {
        viewModelScope.launch {
            firestoreRepository.exitGroup(groupId) { success ->
                if (success) {
                    Toast.makeText(context, "exited successfully", Toast.LENGTH_SHORT).show()
                    innerNavController.navigate(GROUP_SCREEN)
                } else {
                    Toast.makeText(context, "Failed to exit group", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}