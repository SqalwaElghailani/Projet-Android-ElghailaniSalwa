package com.example.my_projet

import app.cash.turbine.test
import com.example.my_projet.data.Entities.Product
import com.example.my_projet.data.Repository.ProductRepository
import com.example.my_projet.ui.product.ProductIntent
import com.example.my_projet.ui.product.ProductViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private lateinit var viewModel: ProductViewModel
    private lateinit var repository: ProductRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = ProductViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test LoadProducts updates state with products`() = runTest {
        val fakeProducts = listOf(
            Product(id = 1, name = "Naruto", genre = "Action", price = 80),
            Product(id = 2, name = "One Piece", genre = "Aventure", price = 100)
        )
        coEvery { repository.getProducts() } returns fakeProducts

        viewModel.sendIntent(ProductIntent.LoadProducts)
        advanceUntilIdle()

        val state = viewModel.state.first()
        assertEquals(2, state.products.size)
        assertFalse(state.isLoading)
        assertEquals(listOf("Action", "Aventure"), state.genres)
    }

    @Test
    fun `test SelectGenre filters products by genre`() = runTest {
        val fakeProducts = listOf(
            Product(id = 1, name = "Naruto", genre = "Action", price = 80),
            Product(id = 2, name = "Fruit Basket", genre = "Romance", price = 90)
        )
        coEvery { repository.getProducts() } returns fakeProducts

        viewModel.sendIntent(ProductIntent.LoadProducts)
        advanceUntilIdle()

        viewModel.sendIntent(ProductIntent.SelectGenre("Action"))
        advanceUntilIdle()

        val state = viewModel.state.first()
        assertEquals(1, state.filteredProducts.size)
        assertEquals("Naruto", state.filteredProducts.first().name)
    }

    @Test
    fun `test AddToCart increments cart count`() = runTest {
        val initialCount = viewModel.state.first().cartCount
        val product = Product(id = 10, name = "Bleach", genre = "Action", price = 70)

        viewModel.sendIntent(ProductIntent.AddToCart(product))
        advanceUntilIdle()

        val newCount = viewModel.state.first().cartCount
        assertEquals(initialCount + 1, newCount)
    }
}
