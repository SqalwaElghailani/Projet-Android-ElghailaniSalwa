package com.example.my_projet.ui.product.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import com.example.my_projet.data.Entities.Product
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.my_projet.data.Api.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.font.FontWeight
import com.example.my_projet.ui.product.component.MainHeader

@Composable
fun OrderListScreen(
    userId: Int,
    navController: NavController,
    onBack: () -> Unit,
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
    var orders by remember { mutableStateOf(listOf<Order>()) }

    LaunchedEffect(userId) {
        orders = lireCommandesParUser(context, userId)
    }

    Column(
        modifier = Modifier.padding(12.dp)
    ) {
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
            navController = navController,
            onNavigateToFavorites = { navController.navigate("favorites") },
            userId = userId,
            onNavigateToHome = { navController.navigate("home") }
        )

        // ✅ Titre avec flèche de retour
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Retour",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Mes commandes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (orders.isEmpty()) {
            Text("Aucune commande trouvée.")
        } else {
            orders.forEach { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Date: ${order.date}", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Adresse: ${order.address}")
                        Text("Téléphone: ${order.phone}")
                        Text("Prix total: ${order.totalPrice} MAD")
                        Text("Statut: ${order.status}")
                        Text("Paiement: ${order.paymentMethod}")

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(4.dp))

                        Text("Articles:", fontWeight = FontWeight.Medium)
                        order.items.forEach { item ->
                            Text("- ${item.productName} x${item.quantity} (Chapitres: ${item.chapters.joinToString()})")
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()

                        IconButton(
                            onClick = {
                                supprimerCommandeParId(context, order.id)
                                orders = orders.filter { it.id != order.id }
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Supprimer",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

