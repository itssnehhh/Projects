package com.example.contactkeeper.ui.home

import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.AppDispatcher
import com.example.contactkeeper.data.model.Contact
import com.example.contactkeeper.ui.ViewModelTestRule
import com.example.contactkeeper.ui.screen.home.HomeViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

val contact =
    Contact(
        "1",
        "User",
        listOf("9856473210", "9874563210"),
        "user@gmail.com",
        "https://example.com/image.jpg",
        "A+",
        "Unknown"
    )


@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = ViewModelTestRule()

    private var fireStoreService = mock<FireStoreService>()
    private val storageReference = mock<StorageReference>()
    private val firebaseStorage = mock<FirebaseStorage>()
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = AppDispatcher(UnconfinedTestDispatcher())


    @Before
    fun setUp() {
        whenever(firebaseStorage.getReferenceFromUrl(any())).thenReturn(storageReference)
        viewModel = HomeViewModel(fireStoreService, testDispatcher)
    }

    @Test
    fun `getContactList should update contacts and isLoading`() = runTest {
        val mockContacts = listOf(contact)
        `when`(fireStoreService.getContacts()).thenReturn(flowOf(mockContacts))
        viewModel.getContactList()
        val contacts = viewModel.contacts.value
        val isLoading = viewModel.isLoading.value
        assertEquals(contacts, mockContacts)
        assertFalse(isLoading)
    }

    @Test
    fun deleteContact_callsFireStoreServiceDeleteContact() = runTest {
        val contactId = "1"
        viewModel.deleteContact(contactId)
        verify(fireStoreService).deleteContact(contactId)
    }
}