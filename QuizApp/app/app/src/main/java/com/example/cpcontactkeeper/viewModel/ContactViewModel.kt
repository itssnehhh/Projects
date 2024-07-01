package com.example.cpcontactkeeper.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpcontactkeeper.R
import com.example.cpcontactkeeper.data.firebase.FirestoreService
import com.example.cpcontactkeeper.data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactViewModel(context: Context):ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _phoneNo = MutableStateFlow<List<String>>(emptyList())
    val phoneNo: StateFlow<List<String>> = _phoneNo

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl

    val bloodGroups: Array<out String> =
        context.resources.getStringArray(R.array.bloodGroupArray)
    var bloodGroup: String = bloodGroups[0]

    private val _expanded: MutableLiveData<Boolean> = MutableLiveData(false)
    val expanded: LiveData<Boolean> = _expanded

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPhoneNumberChange(index: Int, newPhoneNo: String) {
        _phoneNo.value = _phoneNo.value.toMutableList().also { it[index] = newPhoneNo }
    }

    fun addPhoneNumberField() {
        _phoneNo.value = _phoneNo.value.toMutableList().also { it.add("") }
    }
    fun onExpandedChange(status:Boolean){
        _expanded.value = status
    }

    fun onAddressChange(address: String) {
        _address.value = address
    }

    fun onImageUrlChange(newImageUrl: String) {
        _imageUrl.value = newImageUrl
    }


    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    init {
        viewModelScope.launch {
            FirestoreService.getContacts().collect {
                _contacts.value = it
            }
        }
    }

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            FirestoreService.addContact(contact)
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            FirestoreService.updateContact(contact)
        }
    }

    fun deleteContact(contactId: String) {
        viewModelScope.launch {
            FirestoreService.deleteContact(contactId)
        }
    }

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact

    fun fetchContactById(contactId: String) {
        val contact = _contacts.value.find { it.id == contactId }
        _selectedContact.value = contact
    }
}