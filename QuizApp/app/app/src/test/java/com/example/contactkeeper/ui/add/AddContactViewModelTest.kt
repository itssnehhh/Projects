package com.example.contactkeeper.ui.add

import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.AppDispatcher
import com.example.contactkeeper.ui.ViewModelTestRule
import com.example.contactkeeper.ui.home.contact
import com.example.contactkeeper.ui.screen.addScreen.AddContactViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AddContactViewModelTest {

    @get:Rule
    var rule: TestRule = ViewModelTestRule()

    private val fireStoreService = mock<FireStoreService>()
    private lateinit var viewModel: AddContactViewModel
    private val appDispatcher = AppDispatcher(UnconfinedTestDispatcher())


    @Before
    fun setup() {
        viewModel = AddContactViewModel(fireStoreService, appDispatcher)
    }

    @Test
    fun `test fetchContactById function`() = runTest {
        val mockContacts = listOf(contact)
        val contactsFlow = MutableStateFlow(mockContacts)
        whenever(fireStoreService.getContacts()).thenReturn(contactsFlow)
        advanceUntilIdle()
        viewModel.fetchContactById("1")
        val collectedContacts = viewModel.selectedContact.first()
        assertEquals(mockContacts[0], collectedContacts)
    }

    @Test
    fun `test setValues function`() {
        val contact = contact
        viewModel.setValues(contact)
        assertEquals(contact.name, viewModel.name.value)
        assertEquals(contact.email, viewModel.email.value)
        assertEquals(contact.phoneNumber, viewModel.phoneNo.value)
        assertEquals(contact.profilePicture, viewModel.imageUrl.value)
        assertEquals(contact.bloodGroup, viewModel.bloodGroup.value)
        assertEquals(contact.address, viewModel.address.value)
    }

}