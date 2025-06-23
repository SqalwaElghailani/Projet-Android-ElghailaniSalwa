package com.example.my_projet.ui.product

import com.example.my_projet.data.Entities.Product

data class ProductViewState (
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
