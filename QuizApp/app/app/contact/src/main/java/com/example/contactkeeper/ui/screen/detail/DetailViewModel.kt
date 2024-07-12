package com.example.contactkeeper.ui.screen.detail

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

class DetailViewModel : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact

    private val _showDialog = MutableStateFlow(false)
    val showDialog : StateFlow<Boolean> = _showDialog

    init { getUserList() }

    fun onDialogStatusChange(status:Boolean){
        _showDialog.value = status
    }

    private fun getUserList(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                FireStoreService.getContacts().collect {
                    _contacts.value = it
                }
            }
        }
    }

    fun fetchContactById(contactId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val contact = _contacts.value.find { it.id == contactId }
                _selectedContact.value = contact
            }
        }
    }

    fun deleteContact(contactId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                FireStoreService.deleteContact(contactId)
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