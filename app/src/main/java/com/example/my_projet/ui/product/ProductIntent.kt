package com.example.my_projet.ui.product

import com.example.my_projet.data.Entities.Product

sealed class ProductIntent {
    object LoadProducts : ProductIntent()
    data class SelectGenre(val genre: String) : ProductIntent()
    data class AddToCart(val product: Product) : ProductIntent()
    object ClearGenre : ProductIntent()
    object PreviousTopProduct : ProductIntent()
    object NextTopProduct : ProductIntent()
}
