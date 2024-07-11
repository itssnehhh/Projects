package com.example.contactkeeper.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact

    init { getUserList() }

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

}