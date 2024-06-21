package com.example.cpstoremateapplication.data.local.module

import android.content.Context
import androidx.room.Room
import com.example.cpstoremateapplication.data.local.dao.ProductDao
import com.example.cpstoremateapplication.data.local.database.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ProductDatabaseModule {

    @Provides
    fun productDao(productDatabase: ProductDatabase): ProductDao {
        return productDatabase.productDao()
    }

    @Provides
    @Singleton
    fun getInstance(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ProductDatabase::class.java,
            "product"
        )
            .allowMainThreadQueries()
            .build()
    }
}