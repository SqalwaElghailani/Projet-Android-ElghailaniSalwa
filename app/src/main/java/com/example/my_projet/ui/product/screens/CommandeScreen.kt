package com.example.my_projet.ui.product.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.my_projet.data.Entities.Product
import com.example.my_projet.data.Api.*
import com.example.my_projet.ui.product.component.MainHeader
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun CommandeScreen(
    selectedProducts: List<Product>,
    onBack: () -> Unit,
    onConfirm: (Order) -> Unit,
    navController: NavController,
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
    val status = "en attend"

    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var paymentMode by remember { mutableStateOf("") }

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
            productStates.values.sumOf {
                val base = it.product.price
                val quantity = it.quantity.value
                val chapters = it.selectedChapters.value.size
                var total = base * quantity * chapters
                if (chapters == it.product.chapters) {
                    total = (total * 0.95).toInt()
                }
                total
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 20.dp, bottom = 20.dp)
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

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Confirmer la Commande", style = MaterialTheme.typography.titleLarge)
        }

        Divider(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )

        productStates.values.forEach { sel ->
            val chaptersList = (1..(sel.product.chapters ?: 1)).toList()
            val listState = rememberLazyListState()

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Produit: ${sel.product.name}")
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

                Text("Chapitres:")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val firstVisible = listState.firstVisibleItemIndex
                            listState.animateScrollToItem((firstVisible - 5).coerceAtLeast(0))
                        }
                    }) {
                        Text("\u25C0")
                    }
                    LazyRow(state = listState, modifier = Modifier.weight(1f)) {
                        items(chaptersList) { chNum ->
                            FilterChip(
                                selected = sel.selectedChapters.value.contains(chNum),
                                onClick = {
                                    sel.selectedChapters.value =
                                        if (sel.selectedChapters.value.contains(chNum))
                                            sel.selectedChapters.value - chNum
                                        else
                                            sel.selectedChapters.value + chNum
                                },
                                label = { Text("$chNum") },
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val lastVisible = listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
                            listState.animateScrollToItem((lastVisible).coerceAtMost(chaptersList.lastIndex))
                        }
                    }) {
                        Text("\u25B6")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Text("Téléphone :", modifier = Modifier.width(100.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Text("Adresse :", modifier = Modifier.width(100.dp))
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Total: $totalPrice MAD")

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = paymentMode == "Paiement en ligne", onClick = { paymentMode = "Paiement en ligne" })
            Text("Paiement en ligne")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = paymentMode == "Paiement à la livraison", onClick = { paymentMode = "Paiement à la livraison" })
            Text("Paiement à la livraison")
        }

        Spacer(modifier = Modifier.height(12.dp))
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
    val id: Int = 0,
    val userId: Int,
    val phone: String,
    val address: String,
    val totalPrice: Int,
    val paymentMethod: String,
    val items: List<OrderItem>,
    val status: String,
    val date: String
)
