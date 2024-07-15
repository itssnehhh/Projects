package com.example.contactkeeper.data.model

import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Keep
data class AppDispatcher(
    val IO: CoroutineDispatcher = Dispatchers.IO
)