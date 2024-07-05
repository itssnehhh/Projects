package com.example.cpcontactkeeper.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpcontactkeeper.data.firestore.FireStoreService
import com.example.cpcontactkeeper.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    init {
        fetchContacts()
    }

    private fun fetchContacts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                FireStoreService.getContacts().collect {
                    _contacts.value = it
                }
            }
        }
    }

    fun deleteContact(contactId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                FireStoreService.deleteContact(contactId)
                fetchContacts()  // Re-fetch contacts after deletion
            }
        }
    }

}