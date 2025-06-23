package com.example.my_projet.ui.product.component


import androidx.compose.foundation.ExperimentalFoundationApi
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
fun ProductGrid(products: List<Product>, onClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product = product, onNavigateToDetails = onClick)
        }
    }
}
