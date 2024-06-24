package com.example.cpstoremateapplication.data.repository

import com.example.cpstoremateapplication.data.local.dao.FavouriteProductDao
import com.example.cpstoremateapplication.data.model.FavouriteProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteProductRepository @Inject constructor(private val favouriteProductDao: FavouriteProductDao) {

    val favouriteProductList: Flow<List<FavouriteProduct>> = favouriteProductDao.getFavouriteList()

    suspend fun insert(favouriteProduct: FavouriteProduct) {
        favouriteProductDao.insert(favouriteProduct)
    }

    suspend fun deleteFavList(favouriteProduct: List<FavouriteProduct>){
        favouriteProductDao.deleteList(favouriteProduct)
    }

    suspend fun delete(favouriteProduct: FavouriteProduct) {
        favouriteProductDao.delete(favouriteProduct)
    }

    suspend fun isFavorite(productId: Int): Boolean {
        return favouriteProductDao.isFavorite(productId) != null
    }

}