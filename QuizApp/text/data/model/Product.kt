package com.example.storemateproductapplication.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Product_Table")
data class Product(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: String,
    val description: String,
    val category: String,
    val image: String,
    @Embedded
    val rating: Rating
)

data class Rating(
    val rate: Float,
    val count: Int,
)

