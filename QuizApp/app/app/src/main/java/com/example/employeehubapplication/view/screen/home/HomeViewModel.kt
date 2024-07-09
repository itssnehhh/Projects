package com.example.employeehubapplication.view.screen.home

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.employeehubapplication.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(context: Context) : ViewModel() {

    private val _categoryList =
        MutableStateFlow(context.resources.getStringArray(R.array.homeCategoryList))
    val categoryList: StateFlow<Array<out String>> = _categoryList

    private val _imageList = MutableStateFlow(
        listOf(
            R.drawable.icon6,
            R.drawable.icon3,
            R.drawable.icon2,
            R.drawable.icon7
        )
    )
    val categoryImageList: StateFlow<List<Int>> = _imageList

}