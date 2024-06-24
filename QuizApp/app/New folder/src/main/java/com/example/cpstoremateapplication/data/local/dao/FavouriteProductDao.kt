package com.example.cpstoremateapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cpstoremateapplication.data.model.FavouriteProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteProductDao {

    @Query("SELECT * FROM Favourite_Table")
    fun getFavouriteList(): Flow<List<FavouriteProduct>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteProduct: FavouriteProduct)

    @Delete
    suspend fun deleteList(favouriteProduct: List<FavouriteProduct>)

    @Delete
    suspend fun delete(favouriteProduct: FavouriteProduct)

    @Query("SELECT * FROM Favourite_Table WHERE `Product ID` = :productId LIMIT 1")
    suspend fun isFavorite(productId: Int): FavouriteProduct?
}