package com.example.cpstoremateapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cpstoremateapplication.data.local.dao.FavouriteProductDao
import com.example.cpstoremateapplication.data.model.FavouriteProduct

@Database(entities = [FavouriteProduct::class], version = 1, exportSchema = false)
abstract class FavouriteProductDatabase : RoomDatabase() {
    abstract fun productDao(): FavouriteProductDao
}