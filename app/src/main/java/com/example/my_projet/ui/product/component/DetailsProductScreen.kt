package com.example.my_projet.ui.product.component

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.my_projet.data.Api.*
import com.example.my_projet.data.Entities.CartItem
import com.example.my_projet.data.Entities.Product
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailsScreen(
    product: Product,
    searchTerm: String,
    onSearchChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    cartCount: Int,
    onNavigateToOrders: () -> Unit,
    onNavigateToCart: () -> Unit,
    isUserLoggedIn: Boolean = true,
    onLogout: () -> Unit,
    onNavigateToLogin: () -> Unit,
    navController: NavController,
    userId: Int = -1,
    onNavigateToAccount: () -> Unit
) {
    val context = LocalContext.current
    var isInCart by remember { mutableStateOf(false) }
    var isInJaime by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val cartItems = readCartItems(context)
        isInCart = cartItems.any { it.userId == userId && it.productId == product.id.toString() }

        val jaimeItems = readJaimeItems(context)
        isInJaime = jaimeItems.any { it.userId == userId && it.productId == product.id.toString() }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        MainHeader(
            searchTerm = searchTerm,
            onSearchChange = onSearchChange,
            onSearchSubmit = onSearchSubmit,
            cartCount = cartCount,
            isUserLoggedIn = isUserLoggedIn,
            onNavigateToOrders = onNavigateToOrders,
            onNavigateToCart = onNavigateToCart,
            onLogout = onLogout,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToAccount = onNavigateToAccount,
            onNavigateToFavorites = { navController.navigate("favorites") },
            navController = navController,
            userId = userId,
            onNavigateToHome = { navController.navigate("home") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            val imageResId = navController.context.resources.getIdentifier(
                product.imageUrl ?: "", "drawable", navController.context.packageName
            )

            Image(
                painter = painterResource(id = if (imageResId != 0) imageResId else com.example.my_projet.R.drawable.ml),
                contentDescription = null,
                modifier = Modifier.size(200.dp).padding(end = 16.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = product.name ?: "Nom du produit inconnu",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if ((product.quantity ?: 0) <= 10) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFCDD2), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "⚠️ Quantité faible : ${product.quantity ?: 0}",
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                InfoRow(label = "Prix", value = "${product.price} MAD")
                InfoRow(label = "Genre", value = product.genre ?: "N/A")
                InfoRow(label = "Auteur", value = product.author ?: "N/A")
                InfoRow(label = "Chapitres", value = (product.chapters ?: 0).toString())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Étoile fixe",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(30.dp)
                )
                Text(text = "${product.rating ?: 1} / 10", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Évaluation",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp)
                )
                Text("Évaluation", modifier = Modifier.padding(top = 4.dp))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Panier",
                    tint = if (isInCart) Color(0xFFFF9800) else LocalContentColor.current,
                    modifier = Modifier.size(30.dp).clickable {
                        if (userId == -1) {
                            Toast.makeText(context, "Veuillez vous connecter.", Toast.LENGTH_SHORT).show()
                        } else {
                            if (isInCart) {
                                removeFromCart(context, product.id.toString(), userId)
                                isInCart = false
                            } else {
                                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                val currentDate = sdf.format(Date())
                                val item = CartItem(userId, product.id.toString(), product.name ?: "", "${product.price}", product.imageUrl ?: "", currentDate)
                                CardApi(context, item)
                                isInCart = true
                            }
                        }
                    }
                )
                Text("Panier", modifier = Modifier.padding(top = 4.dp))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (isInJaime) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "J'aime",
                    tint = if (isInJaime) Color.Red else Color(0xFFE91E63),
                    modifier = Modifier.size(30.dp).clickable {
                        if (userId == -1) {
                            Toast.makeText(context, "Veuillez vous connecter.", Toast.LENGTH_SHORT).show()
                        } else {
                            if (isInJaime) {
                                removeFromJaimeApi(context, product.id.toString(), userId)
                                isInJaime = false
                            } else {
                                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                val currentDate = sdf.format(Date())
                                val item = CartItem(userId, product.id.toString(), product.name ?: "", "${product.price}", product.imageUrl ?: "", currentDate)
                                addToJaimeApi(context, item)
                                isInJaime = true
                            }
                        }
                    }
                )
                Text("J'aime", modifier = Modifier.padding(top = 4.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp), contentAlignment = Alignment.Center) {
            Text(text = "Résumé", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        Text(text = product.fullSummary ?: product.description ?: "Aucune description disponible.", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label : ", fontWeight = FontWeight.SemiBold, modifier = Modifier.width(100.dp))
        Text(text = value)
    }
}
