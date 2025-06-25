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
import androidx.compose.material.icons.filled.Delete

@Composable
fun OrderListScreen(
    userId: Int,
    onBack: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    var orders by remember { mutableStateOf(listOf<Order>()) }

    LaunchedEffect(userId) {
        Log.d("OrderListScreen", "Chargement des commandes pour userId=$userId")
        orders = lireCommandesParUser(context, userId)
        Log.d("OrderListScreen", "Commandes chargées: ${orders.size}")
        orders.forEach { order ->
            Log.d("OrderListScreen", "Commande: total=${order.totalPrice}, adresse=${order.address}, paiement=${order.paymentMethod}, items=${order.items.size}")
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Liste des commandes pour l'utilisateur $userId", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (orders.isEmpty()) {
            Text("Aucune commande trouvée.")
            Log.d("OrderListScreen", "Aucune commande trouvée pour userId=$userId")
        } else {
            orders.forEach { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        // 👉 الجزء ديال المعلومات
                        Column(
                            modifier = Modifier.weight(1f) // ياخذ المساحة الكاملة الممكنة
                        ) {
                            Text("la date: ${order.date} ")
                            Text("Adresse: ${order.address}")
                            Text("Telephone: ${order.phone}")
                            Text("Prix Total: ${order.totalPrice} MAD")
                            Text("Status: ${order.status}")
                            Text("Mode de paiement: ${order.paymentMethod}")
                            Text("Items:")
                            order.items.forEach { item ->
                                Text("- ${item.productName} x${item.quantity}, Chapitres: ${item.chapters.joinToString()}")
                            }
                        }

                        // 👉 أيقونة الحذف بلون أحمر
                        IconButton(
                            onClick = {
                                supprimerCommandeParId(context, order.id)
                                orders = orders.filter { it.id != order.id }
                            },
                            modifier = Modifier.align(Alignment.Top)
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            Log.d("OrderListScreen", "Retour demandé")
            onBack()
        }) {
            Text("Retour")
        }
    }
}
