package com.example.platzifakestore.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItem(
    val creationAt: String,
    val category: CategoryItem,
    val description: String,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val title: String,
    val updatedAt: String
) : Parcelable