package com.example.etchatapplication.ui.screen.saveInfo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserInfoSaveViewModel @Inject constructor() : ViewModel() {

    private val fName = MutableStateFlow("")
    val firstName: StateFlow<String> = fName

    private val lName = MutableStateFlow("")
    val lastName: StateFlow<String> = lName

    fun onFirstNameChange(name: String) {
        fName.value = name
    }

    fun onLastNameChange(surname: String) {
        lName.value = surname
    }
}