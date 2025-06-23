package com.example.my_projet.ui.product

sealed class ProductIntent {
    object LoadProducts : ProductIntent()
    data class SelectGenre(val genre: String) : ProductIntent()
    object ClearGenre : ProductIntent()
    object PreviousTopProduct : ProductIntent()
    object NextTopProduct : ProductIntent()
}