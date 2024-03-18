package com.example.platzifakestore.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val creationAt: String,
    val id: Int,
    val image: String,
    val name: String,
    val updatedAt: String
):Parcelable