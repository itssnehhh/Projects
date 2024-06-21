package com.example.cpstoremateapplication.data.repository

import com.example.cpstoremateapplication.data.local.dao.ProductDao
import com.example.storemateproductapplication.data.model.Product
import com.example.cpstoremateapplication.data.network.ProductApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(private val productApiService: ProductApiService, private val productDao: ProductDao) {

    //Online
    suspend fun getProductList(): List<Product> {
        return productApiService.getProductList()
    }


    //Offline
    val offlineProductList: Flow<List<Product>> = productDao.getProductList()

    suspend fun insertProduct(product: List<Product>) {
        productDao.insertProduct(product)
    }

    suspend fun getProductDetail(productId:Int) :Product?{
        return productDao.getProductDetail(productId)
    }

    suspend fun deleteProduct(product: Product){
        productDao.deleteProduct(product)
    }

}