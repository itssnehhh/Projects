package com.example.cpcontactkeeper.data.firebase

import com.example.cpcontactkeeper.data.model.Contact
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object FirestoreService {
    private val db = Firebase.firestore
    private val contactsCollection = db.collection("contacts")

    fun getContacts(): Flow<List<Contact>> = callbackFlow {
        val listener = contactsCollection.addSnapshotListener { value, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val contacts = value?.toObjects(Contact::class.java) ?: listOf()
            trySend(contacts).isSuccess
        }
        awaitClose { listener.remove() }
    }

    suspend fun addContact(contact: Contact) {
        contactsCollection.add(contact)
    }

    suspend fun updateContact(contact: Contact) {
        contactsCollection.document(contact.id).set(contact)
    }

    suspend fun deleteContact(contactId: String) {
        contactsCollection.document(contactId).delete()
    }
}