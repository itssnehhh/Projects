package com.example.employeehubapplication.view.screen.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.employeehubapplication.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(context: Context):ViewModel() {

    private val _optionList = MutableStateFlow(context.resources.getStringArray(R.array.profileOptionList))
    val optionList : StateFlow<Array<out String>> = _optionList

    private val _iconList = MutableStateFlow(listOf(
        R.drawable.icon1,
        R.drawable.icon2,
        R.drawable.icon3,
        R.drawable.icon4,
        R.drawable.icon5,
        R.drawable.icon6,
        R.drawable.icon7
    ))
    val iconList : StateFlow<List<Int>> = _iconList

}