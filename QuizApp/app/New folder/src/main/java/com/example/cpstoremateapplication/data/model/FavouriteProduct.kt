package com.example.cpstoremateapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favourite_Table")
data class FavouriteProduct(
    @ColumnInfo("Id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("Product ID")
    val productId : Int,
    @ColumnInfo("Title")
    val title: String,
    @ColumnInfo("Price")
    val price: String,
    @ColumnInfo("Image")
    val image: String,
)
