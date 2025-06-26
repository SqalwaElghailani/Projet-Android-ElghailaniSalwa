package com.example.my_projet.ui.product.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.my_projet.data.Api.readJaimeItems
import com.example.my_projet.data.Entities.CartItem
import com.example.my_projet.data.Api.removeFromJaimeApi
import com.example.my_projet.ui.product.component.MainHeader

@Composable
fun FavoritesScreen(
    userId: Int,
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
    var favoriteItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }

    fun refreshFavorites() {
        favoriteItems = readJaimeItems(context).filter { it.userId == userId }
    }

    LaunchedEffect(Unit) {
        refreshFavorites()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }
            Text(
                text = "Mes Favoris",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        if (favoriteItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aucun article dans vos favoris.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoriteItems) { item ->
                    FavoriteProductCard(  item = item,
                        context = context,
                        onDelete = { refreshFavorites() },
                        onNavigateToDetails = { productId ->
                            navController.navigate("ProductDetails/$productId")
                        })
                }
            }
        }
    }
}

@Composable
fun FavoriteProductCard(
    item: CartItem,
    context: Context,
    onDelete: () -> Unit,
    onNavigateToDetails: (String) -> Unit // ➕ نمرر الدالة للتنقل للتفاصيل
) {
    val imageResId = context.resources.getIdentifier(item.imageUrl, "drawable", context.packageName)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ✅ الصورة مع التنقل للتفاصيل
            Image(
                painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.ml),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable {
                        onNavigateToDetails(item.productId)
                    },
                contentScale = ContentScale.Crop
            )

            Text(
                text = item.productName,
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.SemiBold
            )

            // ✅ Row فيها السعر + أيقونة الحذف
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.productPrice} MAD",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                IconButton(
                    onClick = {
                        removeFromJaimeApi(context, item.productId, item.userId)
                        onDelete()
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Supprimer",
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
