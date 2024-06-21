package com.example.cpstoremateapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.storemateproductapplication.data.model.FavouriteProduct
import com.example.storemateproductapplication.data.model.Product
import com.example.cpstoremateapplication.data.repository.FavouriteProductRepository
import com.example.cpstoremateapplication.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val favRepository: FavouriteProductRepository,
) : ViewModel() {

   val offlineProductList : LiveData<List<Product>> = productRepository.offlineProductList.asLiveData()

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            try {
                val product = productRepository.getProductList()
                productRepository.insertProduct(product)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun getProductDetail(productId: Int):LiveData<Product?> {
        return liveData{
            emit(productRepository.getProductDetail(productId))
        }
    }

    val allFavProduct: LiveData<List<FavouriteProduct>> =
        favRepository.favouriteProductList.asLiveData()

    fun insert(favouriteProduct: FavouriteProduct) = viewModelScope.launch {
        favRepository.insert(favouriteProduct)
    }

    fun deleteFavList(favouriteProduct: List<FavouriteProduct>) =
         viewModelScope.launch {
            favRepository.deleteFavList(favouriteProduct)
        }


    fun remove(favouriteProduct: FavouriteProduct) = viewModelScope.launch {
        favRepository.delete(favouriteProduct)
    }

    suspend fun isFavorite(productId: Int): Boolean {
        return favRepository.isFavorite(productId)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        productRepository.deleteProduct(product)
    }
}