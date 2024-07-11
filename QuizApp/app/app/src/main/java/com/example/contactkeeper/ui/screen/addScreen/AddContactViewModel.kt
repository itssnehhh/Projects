package com.example.contactkeeper.ui.screen.addScreen

import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.Contact
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class AddContactViewModel : ViewModel() {

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact

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

    private val _bloodGroup = MutableStateFlow("A+")
    val bloodGroup: StateFlow<String> = _bloodGroup

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
        _phoneNo.value = _phoneNo.value.toMutableList().apply {
            add("")
        }
    }

    fun removePhoneNumberField(){
        _phoneNo.value = _phoneNo.value.toMutableList().apply {
            remove("")
        }
    }

    fun onAddressChange(newAddress: String) {
        _address.value = newAddress
    }

    fun onImageUrlChange(newImageUrl: String) {
        _imageUrl.value = newImageUrl
    }

    fun onBloodGroupChange(bloodGroup: String) {
        _bloodGroup.value = bloodGroup
    }

    fun onExpandedChange(expand: Boolean) {
        _expanded.value = expand
    }

    fun uploadImageToFirebase(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
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

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                FireStoreService.addContact(contact)
            }
        }
    }

    private fun updateContact(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                FireStoreService.updateContact(contact)
            }
        }
    }

    fun fetchContactById(contactId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                FireStoreService.getContacts().collect { list ->
                    val contact = list.find { it.id == contactId }
                    _selectedContact.value = contact
                }
            }
        }
    }

    fun setValues(contact: Contact) {
        _name.value = contact.name
        _email.value = contact.email
        _address.value = contact.address
        _imageUrl.value = contact.profilePicture
        _bloodGroup.value = contact.bloodGroup
        _phoneNo.value = contact.phoneNumber.toMutableList()
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPhoneNumberValid(phone: String): Boolean {
        return phone.length == 10 && phone.all { it.isDigit() }
    }

    fun verifyDetails(
        contactID: String,
        name: String,
        email: String,
        phoneNumber: List<String>,
        profilePicture: String,
        bloodGroup: String,
        address: String,
        context: Context,
        navController: NavHostController,
    ) {
        when {
            name.isEmpty() -> {
                Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
            }

            email.isEmpty() || !isEmailValid(email) -> {
                Toast.makeText(context, "Please enter valid email address", Toast.LENGTH_SHORT)
                    .show()
            }

            (phoneNumber.isEmpty() || !isPhoneNumberValid(phoneNumber[0])) -> {
                Toast.makeText(context, "Please enter valid contact number", Toast.LENGTH_SHORT)
                    .show()
            }

            (address.isEmpty()) -> {
                Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
            }

            else -> {
                val newContact = Contact(
                    id = if (contactID == "0") UUID.randomUUID().toString() else contactID,
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber,
                    profilePicture = profilePicture,
                    bloodGroup = bloodGroup,
                    address = address
                )
                if (contactID == "0") {
                    addContact(newContact)
                } else {
                    updateContact(newContact)
                }
                navController.popBackStack()
                Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}