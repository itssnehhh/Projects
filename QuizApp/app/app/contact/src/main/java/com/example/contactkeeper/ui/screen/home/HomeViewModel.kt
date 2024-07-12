package com.example.contactkeeper.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.Contact
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val fireStoreService: FireStoreService) : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    init {
        getContactList()
    }

    fun getContactList() {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val contactList = fireStoreService.getContacts()
                    contactList.collect {
                        _contacts.value = it
                        _isLoading.value = false
                    }
                }catch (e:Exception){
                    Log.d("EXCEPTION",e.message.toString())
                }finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun deleteContact(contactId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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