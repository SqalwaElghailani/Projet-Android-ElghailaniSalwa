package com.example.my_projet.ui.product.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my_projet.data.Entities.Product


@Composable
fun ProductsList(products: List<Product>,
                 onNavigateToDetails: (String) -> Unit,
                 onAddToCart: (Product) -> Unit) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
        items(products) { product ->
            ProductItem(product, onNavigateToDetails,onAddToCart = onAddToCart)

        }
    }
}