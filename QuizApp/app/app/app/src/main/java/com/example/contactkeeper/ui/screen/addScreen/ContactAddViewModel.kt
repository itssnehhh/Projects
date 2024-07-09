package com.example.contactkeeper.ui.screen.addScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactkeeper.R
import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.Contact
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

    private val _phoneNo = MutableStateFlow(listOf(""))
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

    fun onPhoneNumberChange(index: Int, number: String) {
        _phoneNo.value = _phoneNo.value.toMutableList().apply {
            this[index] = number
        }
    }


    fun addPhoneNumberField() {
        _phoneNo.value = _phoneNo.value.toMutableList().apply {
            add("")
        }
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

    fun setValues(contact: Contact) {
        // Set other fields
        _name.value = contact.name
        _email.value = contact.email
        _address.value = contact.address
        _imageUrl.value = contact.profilePicture
        bloodGroup = contact.bloodGroup
        _phoneNo.value = contact.phoneNumber.toMutableList()
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
            withContext(Dispatchers.IO){
                FireStoreService.addContact(contact)
            }
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
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
            withContext(Dispatchers.IO){
                FireStoreService.getContacts().collect{
                    _selectedContact.value = _contacts.value.find{ it.id == contactId }
                }
            }
        }
    }
}