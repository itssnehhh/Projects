package com.example.cpcontactkeeper.ui.screen.addScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpcontactkeeper.R
import com.example.cpcontactkeeper.data.firestore.FireStoreService
import com.example.cpcontactkeeper.data.model.Contact
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactViewModel(context: Context) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    var _phoneNo = MutableStateFlow<List<String>>(emptyList())
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

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPhoneNumberChange(index: Int, newPhoneNo: String) {
        _phoneNo.value = _phoneNo.value.toMutableList().apply { this[index] = newPhoneNo }
    }

    fun addPhoneNumberField() {
        _phoneNo.value += ""
    }

    fun onAddressChange(newAddress: String) {
        _address.value = newAddress
    }

    fun onImageUrlChange(newImageUrl: String) {
        _imageUrl.value = newImageUrl
    }

    fun onExpandedChange(expand: Boolean) {
        _expanded.value = expand
    }

    fun uploadImageToFirebase(uri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }


    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                FireStoreService.addContact(contact)
            }
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                FireStoreService.updateContact(contact)
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

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact

    fun fetchContactById(contactId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                FireStoreService.getContacts().collect {
                    val contact = it.find { it.id == contactId }
                    _selectedContact.value = contact
                }
            }
        }
    }
}