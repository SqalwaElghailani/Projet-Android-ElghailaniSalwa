package com.example.my_projet.ui.product

import com.example.my_projet.data.Entities.Product

data class ProductViewState (
    val searchTerm: String = "",
    val cartCount: Int = 0,
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val topProducts: List<Product> = emptyList(),
    val activeTopIndex: Int = 0,
    val genres: List<String> = emptyList(),
    val selectedGenre: String? = null,
    val error: String? = null,
    val selectedProducts: List<Product> = emptyList()


)
