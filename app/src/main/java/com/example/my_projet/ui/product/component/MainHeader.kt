package com.example.my_projet.ui.product.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight


@Composable
fun MainHeader(
    searchTerm: String,
    onSearchChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    cartCount: Int,
    onNavigateToOrders: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Ligne dyal logo + actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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

            Row {
                TextButton(onClick = onNavigateToOrders) {
                    Text("Mes Commandes")
                }

                Box {
                    IconButton(onClick = onNavigateToCart) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Panier"
                        )
                    }
                    if (cartCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color(0xFFFF6B00), shape = CircleShape)
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
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // La barre de recherche - ligne séparée et horizontale
        TextField(
            value = searchTerm,
            onValueChange = onSearchChange,
            placeholder = { Text("Rechercher des mangas, tomes, figurines...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            // Tu peux ajouter un icône de recherche si tu veux ici
        )
    }
}
