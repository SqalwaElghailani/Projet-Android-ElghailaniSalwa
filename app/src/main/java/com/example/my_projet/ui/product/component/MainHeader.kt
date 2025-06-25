package com.example.my_projet.ui.product.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@Composable
fun MainHeader(
    searchTerm: String,
    onSearchChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    cartCount: Int,
    isUserLoggedIn: Boolean,
    onLogout: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToAccount: () -> Unit,
    navController: NavController,
    userId: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Logo + Menu
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 🌟 Logo
            Text(
                text = buildAnnotatedString {
                    append("Manga")
                    withStyle(style = SpanStyle(color = Color(0xFFFF6B00))) {
                        append("Lighter")
                    }
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // 🧭 Menu
            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(onClick = onNavigateToOrders) {
                    Icon(Icons.Default.List, contentDescription = "Mes Commandes")
                }

                Box {
                    IconButton(onClick = onNavigateToCart) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Panier")
                    }
                    if (cartCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color.Red, shape = CircleShape)
                                .align(Alignment.TopEnd)
                        ) {
                            Text(
                                text = cartCount.toString(),
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                IconButton(onClick = {
                    if (isUserLoggedIn) {
                        onNavigateToAccount()
                    } else {
                        onNavigateToLogin()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Compte"
                    )
                }

                if (isUserLoggedIn) {
                    IconButton(onClick = {
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Déconnexion", tint = Color.Red)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔍 Barre de recherche
        TextField(
            value = searchTerm,
            onValueChange = onSearchChange,
            placeholder = { Text("Rechercher des mangas, tomes...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
