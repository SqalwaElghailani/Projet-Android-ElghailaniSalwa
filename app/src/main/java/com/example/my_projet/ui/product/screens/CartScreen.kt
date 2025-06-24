package com.example.my_projet.ui.product.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.my_projet.R
import com.example.my_projet.data.Api.readCartItems
import com.example.my_projet.data.Api.removeFromCart
import com.example.my_projet.data.Entities.CartItem
import com.example.my_projet.data.Entities.Product
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun CartScreen(
    filteredProducts: List<Product>,
    onBack: () -> Unit,
    onOrder: (List<Product>) -> Unit
) {
    val context = LocalContext.current
    val userId = 1 // ID utilisateur

    val items = remember { mutableStateListOf<CartItem>() }
    val selectedProducts = remember { mutableStateMapOf<String, Boolean>() }

    // Charger les éléments du panier
    LaunchedEffect(Unit) {
        items.clear()
        val loadedItems = readCartItems(context)
        items.addAll(loadedItems)
        Log.d("CartScreen", "Chargé ${loadedItems.size} éléments depuis cart.json")
    }

    // Calculer le total
    val totalPrice = items
        .filter { selectedProducts[it.productId] == true }
        .mapNotNull { cartItem ->
            filteredProducts.find { it.id.toString() == cartItem.productId }?.price
        }.sum()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Text("Mon Panier", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (items.isEmpty()) {
            Text("Votre panier est vide.")
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items.toList(), key = { it.productId }) { item ->
                    val product = filteredProducts.find { it.id.toString() == item.productId }
                    if (product != null) {
                        val isSelected = selectedProducts[item.productId] ?: false

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = {
                                        selectedProducts[item.productId] = it
                                        Log.d("CartScreen", if (it) "Sélectionné: ${product.name}" else "Désélectionné: ${product.name}")
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

                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Nom: ${product.name}")
                                    Text("Prix: ${product.price} MAD")
                                    Text("Ajouté le: ${item.dateAdded}")
                                }

                                IconButton(
                                    onClick = {
                                        items.remove(item)
                                        selectedProducts.remove(item.productId)
                                        removeFromCart(context, item.productId, userId)
                                        Log.d("CartScreen", "Supprimé: ${product.name}")
                                    },
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Supprimer produit",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Total: $totalPrice MAD", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val selected = items.mapNotNull { item ->
                    if (selectedProducts[item.productId] == true) {
                        filteredProducts.find { it.id.toString() == item.productId }
                    } else null
                }
                Log.d("CartScreen", "Commander ${selected.size} produits")
                onOrder(selected)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = selectedProducts.values.any { it }
        ) {
            Text("Commander")
        }
    }
}
