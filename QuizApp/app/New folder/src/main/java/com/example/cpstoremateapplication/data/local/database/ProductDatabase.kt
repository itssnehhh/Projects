package com.example.cpstoremateapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cpstoremateapplication.data.local.dao.ProductDao
import com.example.cpstoremateapplication.data.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}