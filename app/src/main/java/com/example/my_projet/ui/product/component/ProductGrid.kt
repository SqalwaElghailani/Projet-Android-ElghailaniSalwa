package com.example.my_projet.ui.product.component


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my_projet.data.Entities.Product

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductGrid(products: List<Product>,
                onClick: (String) -> Unit,
                onAddToCart: (Product) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(products) { product ->
            ProductItem(
                product = product,
                onNavigateToDetails = onClick,
                onAddToCart = onAddToCart
            )
        }
    }
}