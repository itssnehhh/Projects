package com.example.cpstoremateapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cpstoremateapplication.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM product_table")
    fun getProductList(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: List<Product>)

    @Query("SELECT * FROM product_table WHERE id = :productId")
    fun getProductDetail(productId:Int): Product?

    @Delete
    suspend fun deleteProduct(product: Product)

}