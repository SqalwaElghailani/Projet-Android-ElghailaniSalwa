package com.example.my_projet.ui.product.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.my_projet.data.Entities.Product
import com.example.my_projet.ui.product.component.MainHeader
import com.example.my_projet.ui.product.component.ProductGrid
import com.example.my_projet.ui.product.component.TopBannerWithGenres

@Composable
fun HomeScreen(
    searchTerm: String,
    onSearchChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    cartCount: Int,
    onNavigateToOrders: () -> Unit,
    onNavigateToCart: () -> Unit,
    genres: List<String>,
    selectedGenre: String?,
    topProducts: List<Product>,
    filteredProducts: List<Product>,
    activeTopIndex: Int,
    onGenreSelected: (String) -> Unit,
    onClearGenre: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onPreviousTop: () -> Unit,
    onNextTop: () -> Unit
) {
    Column {
        MainHeader(
            searchTerm = searchTerm,
            onSearchChange = onSearchChange,
            onSearchSubmit = onSearchSubmit,
            cartCount = cartCount,
            onNavigateToOrders = onNavigateToOrders,
            onNavigateToCart = onNavigateToCart
        )
        if (topProducts.isNotEmpty()) {
            TopBannerWithGenres(
                product = topProducts[activeTopIndex],
                genres = genres,
                selectedGenre = selectedGenre,
                onGenreSelected = onGenreSelected,
                onClearGenre = onClearGenre,
                onPreviousTop = onPreviousTop,
                onNextTop = onNextTop
            )
        }

        ProductGrid(products = filteredProducts, onClick = onNavigateToDetails)
    }
}
