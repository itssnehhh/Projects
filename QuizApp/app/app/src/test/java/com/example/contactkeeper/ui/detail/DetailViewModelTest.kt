package com.example.contactkeeper.ui.detail

import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.AppDispatcher
import com.example.contactkeeper.ui.ViewModelTestRule
import com.example.contactkeeper.ui.home.contact
import com.example.contactkeeper.ui.screen.detail.DetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    var rule: TestRule = ViewModelTestRule()

    private lateinit var viewModel: DetailViewModel
    private var fireStoreService = mock<FireStoreService>()
    private val appDispatcher = AppDispatcher(UnconfinedTestDispatcher())

    @Before
    fun setup() {
        whenever(fireStoreService.getContacts()).thenReturn(flow {
            emit(listOf(contact))
        })
        viewModel = DetailViewModel(fireStoreService, appDispatcher)
    }

    @Test
    fun `test getUserList function`() = runTest {
        val mockContacts = listOf(contact)
        val contactsFlow = MutableStateFlow(mockContacts)
        whenever(fireStoreService.getContacts()).thenReturn(contactsFlow)
        viewModel.getUserList()
        val collectedContacts = viewModel.contacts.value.toList()
        assertEquals(mockContacts.first(), collectedContacts.first())
    }

    @Test
    fun `test fetchContactById function`() = runTest {
        viewModel.getUserList()
        advanceUntilIdle()
        viewModel.fetchContactById("1") // Fetch the contact by ID
        assertEquals(contact, viewModel.selectedContact.value)
    }

    @Test
    fun `test deleteContact function`() = runTest {
        val contactId = "1"

        // Use doNothing() for void method
        doNothing().`when`(fireStoreService).deleteContact(contactId)

        // Call the deleteContact function
        viewModel.deleteContact(contactId)

        // You might want to verify that the deleteContact method was called
        verify(fireStoreService).deleteContact(contactId)
    }
    @Test
    fun `test onDialogStatusChange function`() {
        viewModel.onDialogStatusChange(true)
        assertEquals(true, viewModel.showDialog.value)
        viewModel.onDialogStatusChange(false)
        assertEquals(false, viewModel.showDialog.value)
    }
}
