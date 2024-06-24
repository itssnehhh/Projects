package com.example.cpstoremateapplication.ui

import com.example.cpstoremateapplication.data.model.FavouriteProduct
import com.example.cpstoremateapplication.data.model.Product
import com.example.cpstoremateapplication.data.model.Rating
import com.example.cpstoremateapplication.data.repository.FavouriteProductRepository
import com.example.cpstoremateapplication.data.repository.ProductRepository
import com.example.cpstoremateapplication.viewModel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class ProductViewModelTest {

    @get:Rule
    val instantExecutorRule = TestRule()

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var favRepository: FavouriteProductRepository

    private lateinit var viewModel: ProductViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = ProductViewModel(productRepository, favRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getProducts_success() = runTest {
        // Given
        val products = listOf(
            Product(
                id = 1,
                title = "Test Product",
                price = "10",
                image = "",
                description = "",
                category = "",
                rating = Rating(0.0f, 0)
            )
        )
        `when`(productRepository.getProductList()).thenReturn(products)

        // When
        viewModel.getProducts()

        // Then
        verify(productRepository).insertProduct(products)
    }

    @Test
    fun insertFavouriteProduct() = runTest{
        // Given
        val favProduct =
            FavouriteProduct(productId = 1, title = "Test Product", price = "10", image = "")

        // When
        viewModel.insert(favProduct)

        // Then
        verify(favRepository).insert(favProduct)
    }

    @Test
    fun removeFavouriteProduct() = runTest {
        // Given
        val favProduct =
            FavouriteProduct(productId = 1, title = "Test Product", price = "10", image = "")

        // When
        viewModel.remove(favProduct)

        // Then
        verify(favRepository).delete(favProduct)
    }

    @Test
    fun isFavorite_productExists() = runTest {
        // Given
        val productId = 1
        `when`(favRepository.isFavorite(productId)).thenReturn(true)

        // When
        val result = viewModel.isFavorite(productId)

        // Then
        Assert.assertTrue(result)
    }

    @Test
    fun isFavorite_productDoesNotExist() = runTest {
        // Given
        val productId = 1
        `when`(favRepository.isFavorite(productId)).thenReturn(false)

        // When
        val result = viewModel.isFavorite(productId)

        // Then
        Assert.assertFalse(result)
    }

    @Test
    fun deleteProduct() = runTest {
        // Given
        val product =  Product(
            id = 1,
            title = "Test Product",
            price = "10",
            image = "",
            description = "",
            category = "",
            rating = Rating(0.0f, 0)
        )
        // When
        viewModel.deleteProduct(product)

        // Then
        verify(productRepository).deleteProduct(product)
    }
}