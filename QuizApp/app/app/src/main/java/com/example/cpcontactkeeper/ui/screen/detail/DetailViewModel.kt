package com.example.cpcontactkeeper.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpcontactkeeper.data.firestore.FireStoreService
import com.example.cpcontactkeeper.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel : ViewModel() {


    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                FireStoreService.getContacts().collect {
                    _contacts.value = it
                }
            }
        }
    }

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact

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