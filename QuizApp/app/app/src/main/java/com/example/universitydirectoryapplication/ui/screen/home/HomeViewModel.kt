package com.example.universitydirectoryapplication.ui.screen.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.universitydirectoryapplication.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(context: Context) : ViewModel() {

    private val _imageList = MutableStateFlow(
        listOf(R.drawable.university1, R.drawable.university2, R.drawable.university3)
    )
    val imageList: StateFlow<List<Int>> = _imageList

    private val _titleList =
        MutableStateFlow(context.resources.getStringArray(R.array.universityTitleList))
    val titleList: StateFlow<Array<out String>> = _titleList

    private val _cityList =
        MutableStateFlow(context.resources.getStringArray(R.array.universityCityList))
    val cityList: StateFlow<Array<out String>> = _cityList

    private val _categoryList =
        MutableStateFlow(context.resources.getStringArray(R.array.categoryList))
    val categoryList: StateFlow<Array<out String>> = _categoryList

    private val _catImageList = MutableStateFlow(
        listOf(
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.cat3,
            R.drawable.cat4,
            R.drawable.cat5,
            R.drawable.cat6
        )
    )
    val catImageList: StateFlow<List<Int>> = _catImageList

    private val _articleList =
        MutableStateFlow(context.resources.getStringArray(R.array.articlesList))
    val articleList: StateFlow<Array<out String>> = _articleList

    private val _articleImageList = MutableStateFlow(
        listOf(R.drawable.article1, R.drawable.article2, R.drawable.article3)
    )
    val articleImageList: StateFlow<List<Int>> = _articleImageList

}