package com.example.contactkeeper.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val fireStoreService: FireStoreService) : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    init {
        getContactList()
    }

    fun getContactList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val contactList = fireStoreService.getContacts()
                contactList.collect { _contacts.value = it }
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
}