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
    onNavigateToFavorites: () -> Unit,
    navController: NavController,
    userId: Int,
    onNavigateToHome: () -> Unit
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = onNavigateToHome) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Accueil",
                    tint = if (currentRoute == "home") Color(0xFFFF6B00) else Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                IconButton(onClick = onNavigateToFavorites) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favoris",
                        tint = if (currentRoute == "favorites") Color(0xFFFF6B00) else Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Box {
                    IconButton(onClick = onNavigateToCart) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Panier",
                            tint = if (currentRoute == "cart") Color(0xFFFF6B00) else Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
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
                IconButton(onClick = onNavigateToOrders) {
                    Icon(
                        Icons.Default.Receipt,
                        contentDescription = "Mes Commandes",
                        tint = if (currentRoute?.startsWith("orders/$userId")== true) Color(0xFFFF6B00) else Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(onClick = {
                    if (isUserLoggedIn) onNavigateToAccount()
                    else onNavigateToLogin()
                }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Compte",
                        tint = if (currentRoute == "account") Color(0xFFFF6B00) else Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }

//                if (isUserLoggedIn) {
//                    IconButton(onClick = { onLogout() }) {
//                        Icon(
//                            Icons.Default.Logout,
//                            contentDescription = "Déconnexion",
//                            tint = Color.Red
//                        )
//                    }
//                }
            }
        }
    }
}


        //Spacer(modifier = Modifier.height(12.dp))

//        TextField(
//            value = searchTerm,
//            onValueChange = onSearchChange,
//            placeholder = { Text("Rechercher des mangas, tomes...") },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth()
//        )


@Composable
fun SearchBar(
    searchTerm: String,
    onSearchChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val orangeColor = Color(0xFFFF6F00)

    OutlinedTextField(
        value = searchTerm,
        onValueChange = onSearchChange,
        placeholder = { Text("  ") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = orangeColor
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = orangeColor,
            unfocusedBorderColor = orangeColor,
            cursorColor = orangeColor,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedLabelColor = orangeColor
        ),
        shape = MaterialTheme.shapes.medium
    )
}
