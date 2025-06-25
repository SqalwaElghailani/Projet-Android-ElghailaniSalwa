package com.example.my_projet.ui.product.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.my_projet.data.Entities.Product
import com.example.my_projet.ui.product.component.MainHeader
import com.example.my_projet.ui.product.component.ProductGrid
import com.example.my_projet.ui.product.component.TopBannerWithGenres

@Composable
fun HomeScreen(
    searchTerm: String,
    onSearchChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    cartCount: Int,
    onNavigateToOrders: () -> Unit,
    onNavigateToCart: () -> Unit,
    genres: List<String>,
    selectedGenre: String?,
    topProducts: List<Product>,
    filteredProducts: List<Product>,
    activeTopIndex: Int,
    onGenreSelected: (String) -> Unit,
    onClearGenre: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onPreviousTop: () -> Unit,
    onNextTop: () -> Unit,
    onAddToCart: (Product) -> Unit,
    navController: NavController,
    userId: Int = 1,
    isUserLoggedIn: Boolean = true,
    onLogout: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
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
            onNavigateToCart = {
                Log.d("NAVIGATION", "Navigating to cart screen from HomeScreen")
                onNavigateToCart()
            },
            onLogout = {
                Log.d("AUTH", "Déconnexion depuis HomeScreen...")
                onLogout() // كتنفذ تسجيل الخروج من AppNavigation
            },
            onNavigateToLogin = {
                Log.d("AUTH", "Navigating to Login from HomeScreen...")
                onNavigateToLogin()
            },
            onNavigateToAccount = {
                Log.d("AUTH", "Navigating to Account from HomeScreen...")
                onNavigateToAccount()
            },
            navController = navController,
            userId = userId
        )

        if (topProducts.isNotEmpty()) {
            TopBannerWithGenres(
                product = topProducts[activeTopIndex],
                genres = genres,
                selectedGenre = selectedGenre,
                onGenreSelected = onGenreSelected,
                onClearGenre = onClearGenre,
                onPreviousTop = onPreviousTop,
                onNextTop = onNextTop
            )
        }

        ProductGrid(
            products = filteredProducts,
            onClick = onNavigateToDetails,
            onAddToCart = onAddToCart
        )
    }
}
