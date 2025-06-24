package com.example.my_projet.ui.product.screens
// Compose UI
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Project-specific
import com.example.my_projet.data.Api.readCartItems
import com.example.my_projet.data.Api.removeFromCart
import com.example.my_projet.data.Entities.CartItem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.my_projet.R
import com.example.my_projet.data.Entities.Product
import android.util.Log

@Composable
fun CartScreen(
    filteredProducts: List<Product>,
    onBack: () -> Unit,
    onOrder: (List<Product>) -> Unit // باش نمشيو لصفحة الكوموند
) {
    val context = LocalContext.current
    val items = remember { mutableStateListOf<CartItem>() }
    val selectedProducts = remember { mutableStateListOf<Product>() }

    // Load items from JSON
    LaunchedEffect(Unit) {
        items.clear()
        val loadedItems = readCartItems(context)
        Log.d("CartScreen", "Loaded ${loadedItems.size} items from cart JSON")
        items.addAll(loadedItems)
    }

    // Total Price
    val totalPrice = selectedProducts.sumOf { it.price }
    Log.d("CartScreen", "Total price recalculated: $totalPrice MAD")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Text("Mon Panier", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        items.forEach { item ->
            val product = filteredProducts.find { it.id.toString() == item.productId }

            if (product != null) {
                var isSelected by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                isSelected = it
                                if (it) {
                                    selectedProducts.add(product)
                                    Log.d("CartScreen", "Selected product: ${product.name}")
                                } else {
                                    selectedProducts.remove(product)
                                    Log.d("CartScreen", "Deselected product: ${product.name}")
                                }
                            }
                        )

                        val imageRes = context.resources.getIdentifier(
                            product.imageUrl, "drawable", context.packageName
                        )

                        Image(
                            painter = painterResource(id = if (imageRes != 0) imageRes else R.drawable.ml),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(end = 8.dp)
                        )

                        Column {
                            Text("Nom: ${product.name}")
                            Text("Prix: ${product.price} MAD")
                            Text("Ajouté le: ${item.dateAdded}")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Total + bouton commander
        Text("Total: $totalPrice MAD", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                Log.d("CartScreen", "Commander clicked with ${selectedProducts.size} products selected")
                onOrder(selectedProducts)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = selectedProducts.isNotEmpty()
        ) {
            Text("Commander")
        }
    }
}
