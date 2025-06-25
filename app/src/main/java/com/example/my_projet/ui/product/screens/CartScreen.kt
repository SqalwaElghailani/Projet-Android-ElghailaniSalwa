package com.example.my_projet.ui.product.screens

import android.content.Context
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
import androidx.navigation.NavController
import com.example.my_projet.R
import com.example.my_projet.data.Api.readCartItems
import com.example.my_projet.data.Api.removeFromCart
import com.example.my_projet.data.Entities.CartItem
import com.example.my_projet.data.Entities.Product
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import android.util.Log

@Composable
fun CartScreen(
    filteredProducts: List<Product>,
    navController: NavController,
    onOrder: (List<Product>) -> Unit
) {
    val context = LocalContext.current

    // جلب userId من SharedPreferences
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = prefs.getInt("user_id", -1)

    Log.d("CartScreen", "SharedPreferences userId = $userId")

    // إذا ماكانش مسجل، نوجه المستخدم لصفحة تسجيل الدخول
    LaunchedEffect(userId) {
        if (userId == -1) {
            Log.d("CartScreen", "User not logged in, navigating to login")
            navController.navigate("login") {
                popUpTo("cart") { inclusive = true }
            }
        }
    }

    val items = remember { mutableStateListOf<CartItem>() }
    val selectedProducts = remember { mutableStateMapOf<String, Boolean>() }

    // تحميل العناصر فقط إذا المستخدم متصل
    LaunchedEffect(userId) {
        if (userId != -1) {
            val loadedItems = readCartItems(context)
            Log.d("CartScreen", "readCartItems returned: ${loadedItems.size} items")
            val userItems = loadedItems.filter { it.userId == userId }
            Log.d("CartScreen", "Filtered for userId=$userId: ${userItems.size} items")

            items.clear()
            items.addAll(userItems)
        }
    }

    Log.d("CartScreen", "filteredProducts.size = ${filteredProducts.size}")

    val totalPrice = items
        .filter { selectedProducts[it.productId] == true }
        .mapNotNull { cartItem ->
            filteredProducts.find { it.id.toString() == cartItem.productId }?.price
        }.sum()

    if (userId != -1) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Text("Mon Panier", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            if (items.isEmpty()) {
                Log.d("CartScreen", "Panier vide")
                Text("Votre panier est vide.")
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items.toList(), key = { it.productId }) { item ->
                        val product = filteredProducts.find { it.id.toString() == item.productId }

                        if (product == null) {
                            Log.w("CartScreen", "Product not found for productId: ${item.productId}")
                        }

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
                                            Log.d("CartScreen", "Checkbox changed for ${item.productId}: $it")
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
                                            Log.d("CartScreen", "Removed ${item.productId} from cart")
                                            items.remove(item)
                                            selectedProducts.remove(item.productId)
                                            removeFromCart(context, item.productId, userId)
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
}

