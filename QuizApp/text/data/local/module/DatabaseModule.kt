package com.example.cpstoremateapplication.data.local.module

import android.content.Context
import androidx.room.Room
import com.example.cpstoremateapplication.data.local.dao.FavouriteProductDao
import com.example.cpstoremateapplication.data.local.database.FavouriteProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun favProductDao(favouriteProductDatabase: FavouriteProductDatabase): FavouriteProductDao {
        return favouriteProductDatabase.productDao()
    }

    @Provides
    @Singleton
    fun getInstance(@ApplicationContext context: Context): FavouriteProductDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FavouriteProductDatabase::class.java,
            "favourite_product"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}