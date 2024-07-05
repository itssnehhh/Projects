package com.example.cpcontactkeeper.data.firestore

import android.util.Log
import com.example.cpcontactkeeper.data.model.Contact
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object FireStoreService {


    private val db = FirebaseFirestore.getInstance()
    private val contactsCollection = db.collection("contacts")


    fun getContacts(): Flow<List<Contact>> = callbackFlow {
        val listener = contactsCollection.addSnapshotListener { value, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val contacts = value?.documents?.mapNotNull { document ->
                val contact = document.toObject(Contact::class.java)
                contact?.copy(id = document.id) // Manually set the document ID
            } ?: listOf()
            trySend(contacts).isSuccess
        }
        awaitClose { listener.remove() }
    }

    fun addContact(contact: Contact) {
        contactsCollection.add(contact)
            .addOnSuccessListener { documentReference ->
                Log.d("ADD", "ContactSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("ADD", "Error adding contact", e)
            }
    }

    fun updateContact(contact: Contact) {
        contactsCollection.document(contact.id)
            .set(contact)
            .addOnSuccessListener {
                Log.d("UPDATE-SUCCESS", "Contact successfully updated!")
            }.addOnFailureListener {
                Log.d("UPDATE-FAIL", "Contact failed to update!")
            }
    }

    fun deleteContact(contactId: String) {
        contactsCollection.document(contactId)
            .delete()
            .addOnSuccessListener {
                Log.d("FireStoreService", "Contact successfully deleted with ID: $contactId")
            }
            .addOnFailureListener { e ->
                Log.w("FireStoreService", "Error deleting contact with ID: $contactId", e)
            }
    }
}