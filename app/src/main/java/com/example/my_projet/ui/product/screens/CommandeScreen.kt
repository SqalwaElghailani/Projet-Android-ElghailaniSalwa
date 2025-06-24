package com.example.my_projet.ui.product.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import com.example.my_projet.data.Entities.Product
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.my_projet.data.Api.*
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun CommandeScreen(
    selectedProducts: List<Product>,
    onBack: () -> Unit,
    onConfirm: (Order) -> Unit,
    navController: NavController,
    userId: Int = 1
) {
    val status = "en attend"
    val context = LocalContext.current
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var paymentMode by remember { mutableStateOf("") }

    Log.d("CommandeScreen", "Screen started with ${selectedProducts.size} selected products")

    val productStates = remember {
        selectedProducts.associate { product ->
            val lastChapter = product.chapters ?: 1
            product.id to ProductSelection(
                product = product,
                quantity = mutableStateOf(1),
                selectedChapters = mutableStateOf(setOf(lastChapter))
            )
        }
    }

    val totalPrice by remember {
        derivedStateOf {
            val total = productStates.values.sumOf {
                val base = it.product.price
                val quantity = it.quantity.value
                val chapters = it.selectedChapters.value.size
                var total = base * quantity * chapters
                if (chapters == it.product.chapters) {
                    total = (total * 0.95).toInt()
                }
                total
            }
            Log.d("CommandeScreen", "Total price recalculated: $total")
            total
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Confirmer la Commande", style = MaterialTheme.typography.titleLarge)

        productStates.values.forEach { sel ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Produit: ${sel.product.name}", style = MaterialTheme.typography.bodyLarge)
                Text("Prix unitaire: ${sel.product.price} MAD")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Quantité: ")
                    IconButton(onClick = {
                        if (sel.quantity.value > 1) sel.quantity.value--
                    }) { Text("-") }
                    Text(sel.quantity.value.toString())
                    IconButton(onClick = {
                        if (sel.quantity.value < (sel.product.quantity ?: 1)) sel.quantity.value++
                    }) { Text("+") }
                }

                Text("Chapitres (max ${sel.product.chapters}):")
                Row {
                    (1..(sel.product.chapters ?: 1)).forEach { chNum ->
                        FilterChip(
                            selected = sel.selectedChapters.value.contains(chNum),
                            onClick = {
                                sel.selectedChapters.value =
                                    if (sel.selectedChapters.value.contains(chNum))
                                        sel.selectedChapters.value - chNum
                                    else
                                        sel.selectedChapters.value + chNum
                            },
                            label = { Text("$chNum") }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Téléphone") }
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Adresse") }
        )

        Text("Total: $totalPrice MAD", style = MaterialTheme.typography.bodyLarge)

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = paymentMode == "en_ligne",
                onClick = { paymentMode = "en_ligne" }
            )
            Text("Paiement en ligne")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = paymentMode == "a_la_livraison",
                onClick = { paymentMode = "a_la_livraison" }
            )
            Text("Paiement à la livraison")
        }

        Button(
            onClick = {
                val items = productStates.values.map {
                    OrderItem(
                        productId = it.product.id,
                        productName = it.product.name ?: "",
                        quantity = it.quantity.value,
                        chapters = it.selectedChapters.value.toList(),
                        unitPrice = it.product.price
                    )
                }

                val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

                val order = Order(
                    userId = userId,
                    phone = phone.text,
                    address = address.text,
                    totalPrice = totalPrice,
                    paymentMethod = paymentMode,
                    items = items,
                    status = status,
                    date = currentDate
                )

                onConfirm(order)
                enregistrerCommande(context, order)
                navController.navigate("orders/$userId") {
                    popUpTo("commande") { inclusive = true }
                }
            },
            enabled = phone.text.isNotBlank() && address.text.isNotBlank() && paymentMode.isNotEmpty()
        ) {
            Text("Confirmer la commande")
        }
    }
}

// ======================== Data Classes ========================

data class ProductSelection(
    val product: Product,
    var quantity: MutableState<Int>,
    var selectedChapters: MutableState<Set<Int>>
)

data class OrderItem(
    val productId: Int,
    val productName: String,
    val quantity: Int,
    val chapters: List<Int>,
    val unitPrice: Int
)

data class Order(
    val userId: Int,
    val phone: String,
    val address: String,
    val totalPrice: Int,
    val paymentMethod: String,
    val items: List<OrderItem>,
    val status: String,
    val date: String // ✅ تاريخ الطلب
)
