package com.example.contactkeeper.ui.screen.addScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.contactkeeper.R
import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.AppDispatcher
import com.example.contactkeeper.data.model.Contact
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val fireStoreService: FireStoreService,
    private val appDispatcher: AppDispatcher
) : ViewModel() {

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

    fun removePhoneNumberField() {
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
            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                onSuccess(downloadUri.toString())
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            withContext(appDispatcher.IO) {
                uploadImageToFirebase(
                    Uri.parse(contact.profilePicture),
                    onSuccess = { imageUrl ->
                        contact.profilePicture = imageUrl
                        fireStoreService.addContact(contact)
                    },
                    onFailure = { exception ->
                        exception.message
                    }
                )
            }
        }
    }

    private fun updateContact(contact: Contact) {
        viewModelScope.launch {
            withContext(appDispatcher.IO) {
                if (contact.profilePicture.startsWith("content://")) {
                    uploadImageToFirebase(
                        Uri.parse(contact.profilePicture),
                        onSuccess = { imageUrl ->
                            contact.profilePicture = imageUrl
                            fireStoreService.updateContact(contact)
                        },
                        onFailure = { exception ->
                            Log.d("EXCEPTION", "updateContact: ${exception.message}")
                        }
                    )
                } else {
                    fireStoreService.updateContact(contact)
                }
            }
        }
    }

    fun fetchContactById(contactId: String) {
        viewModelScope.launch {
            withContext(appDispatcher.IO) {
                fireStoreService.getContacts().collect { list ->
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
    ): Boolean {
        return when {
            name.isEmpty() -> {
                Toast.makeText(context, context.getString(R.string.empty_toast), Toast.LENGTH_SHORT)
                    .show()
                false
            }

            email.isEmpty() || !isEmailValid(email) -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.invalid_email_toast), Toast.LENGTH_SHORT
                )
                    .show()
                false
            }

            (phoneNumber.isEmpty() || !isPhoneNumberValid(phoneNumber[0])) -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.invalid_contact_toast), Toast.LENGTH_SHORT
                )
                    .show()
                false
            }

            (address.isEmpty()) -> {
                Toast.makeText(context, context.getString(R.string.empty_toast), Toast.LENGTH_SHORT)
                    .show()
                false
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
                Toast.makeText(context, context.getString(R.string.save_toast), Toast.LENGTH_SHORT)
                    .show()
                true
            }
        }
    }
}
