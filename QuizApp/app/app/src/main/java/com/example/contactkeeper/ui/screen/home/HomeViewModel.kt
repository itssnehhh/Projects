package com.example.contactkeeper.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.AppDispatcher
import com.example.contactkeeper.data.model.Contact
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fireStoreService: FireStoreService,
    private val appDispatcher: AppDispatcher,
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getContactList()
    }

    fun getContactList() {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(appDispatcher.IO) {
                try {
                    val contactList = fireStoreService.getContacts()
                    contactList.collect {
                        _contacts.value = it
                        _isLoading.value = false
                    }
                } catch (e: Exception) {
                    e.message
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun deleteContact(contactId: String) {
        viewModelScope.launch {
            withContext(appDispatcher.IO) {
                fireStoreService.deleteContact(contactId)
            }
        }
    }

    fun deleteImageFromFirebase(imageUrl: String) {
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
        storageRef.delete()
            .addOnSuccessListener {
                Log.d("DELETE", "deleteImageFromFirebase: Success")
            }
            .addOnFailureListener { exception ->
                Log.d("DELETE", "deleteImageFromFirebase: ${exception.message}")
            }
    }
}