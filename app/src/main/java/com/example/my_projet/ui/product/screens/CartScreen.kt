package com.example.my_projet.ui.product.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.my_projet.R
import com.example.my_projet.data.Api.readCartItems
import com.example.my_projet.data.Api.removeFromCart
import com.example.my_projet.data.Entities.CartItem
import com.example.my_projet.data.Entities.Product
import com.example.my_projet.ui.product.component.MainHeader
import androidx.compose.foundation.clickable


@Composable
fun CartScreen(
    filteredProducts: List<Product>,
    navController: NavController,
    onOrder: (List<Product>) -> Unit,
    isUserLoggedIn: Boolean,
    cartCount: Int,
    searchTerm: String,
    onSearchChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = prefs.getInt("user_id", -1)

    val items = remember { mutableStateListOf<CartItem>() }
    val selectedProducts = remember { mutableStateMapOf<String, Boolean>() }
    val visibleCheckbox = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(userId) {
        if (userId == -1) {
            navController.navigate("login") {
                popUpTo("cart") { inclusive = true }
            }
        } else {
            val loadedItems = readCartItems(context).filter { it.userId == userId }
            items.clear()
            items.addAll(loadedItems)
        }
    }

    val totalPrice = items
        .filter { selectedProducts[it.productId] == true }
        .mapNotNull { cartItem ->
            filteredProducts.find { it.id.toString() == cartItem.productId }?.price
        }.sum()

    Column(modifier = Modifier.padding(12.dp)) {
        MainHeader(
            searchTerm = searchTerm,
            onSearchChange = onSearchChange,
            onSearchSubmit = onSearchSubmit,
            cartCount = cartCount,
            isUserLoggedIn = isUserLoggedIn,
            onNavigateToOrders = onNavigateToOrders,
            onNavigateToCart = onNavigateToCart,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToAccount = onNavigateToAccount,
            onNavigateToFavorites = onNavigateToFavorites,
            onLogout = onLogout,
            navController = navController,
            userId = userId,
            onNavigateToHome = { navController.navigate("home") }
        )

        // ✅ السهم والعنوان
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }
            Text(
                text = "Mon Panier",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (items.isEmpty()) {
            Text("Votre panier est vide.")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items) { item ->
                    val product = filteredProducts.find { it.id.toString() == item.productId }
                    if (product != null) {
                        CartProductCard(
                            item = item,
                            product = product,
                            context = context,
                            isSelected = selectedProducts[item.productId] ?: false,
                            showCheckbox = visibleCheckbox[item.productId] ?: false,
                            onCheck = { selectedProducts[item.productId] = it },
                            onDoubleClick = { visibleCheckbox[item.productId] = true },
                            onDelete = {
                                items.remove(item)
                                selectedProducts.remove(item.productId)
                                visibleCheckbox.remove(item.productId)
                                removeFromCart(context, item.productId, userId)
                            },
                            onNavigateToDetails = {
                                navController.navigate("ProductDetails/${item.productId}")
                            }
                        )
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
                    if (selectedProducts[item.productId] == true)
                        filteredProducts.find { it.id.toString() == item.productId }
                    else null
                }
                onOrder(selected)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = selectedProducts.values.any { it }
        ) {
            Text("Commander")
        }
    }
}


@Composable
fun CartProductCard(
    item: CartItem,
    product: Product,
    context: Context,
    isSelected: Boolean,
    showCheckbox: Boolean,
    onCheck: (Boolean) -> Unit,
    onDoubleClick: () -> Unit,
    onDelete: () -> Unit,
    onNavigateToDetails: () -> Unit
) {
    val imageResId = context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)

    var clickCount by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                clickCount++
                if (clickCount == 2) {
                    onDoubleClick()
                    clickCount = 0
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showCheckbox) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onCheck,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Image(
                painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.ml),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable { onNavigateToDetails() },
                contentScale = ContentScale.Crop
            )

            Text("${product.name}", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${product.price} MAD", color = Color.Gray, fontSize = 14.sp)

                IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Red, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

