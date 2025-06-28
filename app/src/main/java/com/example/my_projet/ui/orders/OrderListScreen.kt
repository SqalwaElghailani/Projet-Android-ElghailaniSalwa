package com.example.my_projet.ui.orders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.my_projet.data.Api.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import com.example.my_projet.R
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

        // Titre avec flèche de retour
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.retour),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = stringResource(R.string.mes_commandes),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (orders.isEmpty()) {
            Text(stringResource(R.string.aucune_commande_trouvee))
        } else {
            orders.forEach { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = stringResource(R.string.date_colon, order.date),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = stringResource(R.string.adresse_formatee, order.address))
                        Text(text = stringResource(R.string.telephone_formatee, order.phone))
                        Text(text = stringResource(R.string.prix_total_colon, order.totalPrice))
                        Text(text = stringResource(R.string.statut_colon, order.status))
                        Text(text = stringResource(R.string.paiement_colon, order.paymentMethod))

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(stringResource(R.string.articles_colon), fontWeight = FontWeight.Medium)
                        order.items.forEach { item ->
                            Text("- ${item.productName} x${item.quantity} (${stringResource(R.string.chapitres)}: ${item.chapters.joinToString()})")
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
                                contentDescription = stringResource(R.string.supprimer),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
