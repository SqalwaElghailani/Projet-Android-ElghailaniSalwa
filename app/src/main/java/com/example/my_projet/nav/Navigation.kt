package com.example.my_projet.nav

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.my_projet.ui.product.ProductIntent
import com.example.my_projet.ui.product.ProductViewModel
import com.example.my_projet.ui.product.component.DetailsScreen
import com.example.my_projet.ui.product.screens.CartScreen
import com.example.my_projet.ui.product.screens.CommandeScreen
import com.example.my_projet.ui.product.screens.HomeScreen
import com.example.my_projet.ui.product.screens.OrderListScreen
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.my_projet.ui.product.screens.AccountScreen
import com.example.my_projet.ui.product.screens.FavoritesScreen
import com.example.my_projet.ui.product.screens.LoginScreen
import com.example.my_projet.ui.product.screens.RegisterScreen

object Routes {
    const val Home = "home"
    const val ProductDetails = "productDetails"
}

@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val context = LocalContext.current
    val navController = rememberNavController()

    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val isUserLoggedIn = prefs.getBoolean("logged_in", false)
    val userId = prefs.getInt("user_id", -1)

    val uiState by viewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = Routes.Home) {

        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { loggedUser ->
                    prefs.edit().putBoolean("logged_in", true).putInt("user_id", loggedUser.id).apply()
                    navController.navigate(Routes.Home) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Home) {
            HomeScreen(
                searchTerm = uiState.searchTerm,
                onSearchChange = { viewModel.onSearchChange(it) },
                onSearchSubmit = { viewModel.onSearchSubmit() },
                cartCount = uiState.cartCount,
                onNavigateToOrders = {
                    if (isUserLoggedIn && userId != -1) {
                        navController.navigate("orders/$userId")
                    } else {
                        navController.navigate("login")
                    }
                },
                onNavigateToCart = { navController.navigate("cart") },
                genres = uiState.genres,
                selectedGenre = uiState.selectedGenre,
                topProducts = uiState.topProducts,
                filteredProducts = uiState.filteredProducts,
                activeTopIndex = uiState.activeTopIndex,
                onGenreSelected = { genre -> viewModel.sendIntent(ProductIntent.SelectGenre(genre)) },
                onClearGenre = { viewModel.sendIntent(ProductIntent.ClearGenre) },
                onNavigateToDetails = { productId -> navController.navigate("${Routes.ProductDetails}/$productId") },
                onPreviousTop = { viewModel.sendIntent(ProductIntent.PreviousTopProduct) },
                onNextTop = { viewModel.sendIntent(ProductIntent.NextTopProduct) },
                onAddToCart = { product -> viewModel.sendIntent(ProductIntent.AddToCart(product)) },
                navController = navController,
                userId = userId,
                isUserLoggedIn = isUserLoggedIn,
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToAccount = {
                    if (isUserLoggedIn && userId != -1) navController.navigate("account")
                    else navController.navigate("login")
                },
                onLogout = {
                    prefs.edit().clear().apply()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Routes.ProductDetails}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val product = uiState.products.find { it.id.toString() == productId }

            if (product != null) {
                DetailsScreen(product = product,
                        searchTerm = uiState.searchTerm,
                    onSearchChange = { viewModel.onSearchChange(it) },
                    onSearchSubmit = { viewModel.onSearchSubmit() },
                    cartCount = uiState.cartCount,
                    onNavigateToOrders = {
                        if (isUserLoggedIn && userId != -1) {
                            navController.navigate("orders/$userId")
                        } else {
                            navController.navigate("login")
                        }
                    },
                    onNavigateToCart = { navController.navigate("cart") },
                    navController = navController,
                    userId = userId,
                    isUserLoggedIn = isUserLoggedIn,
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToAccount = {
                        if (isUserLoggedIn && userId != -1) navController.navigate("account")
                        else navController.navigate("login")
                    },
                    onLogout = {
                        prefs.edit().clear().apply()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    })
            } else {

                androidx.compose.material3.Text(
                    text = "Produit non trouvé.",
                    modifier = androidx.compose.ui.Modifier.fillMaxSize().padding(16.dp)
                )
            }
        }


        composable("cart") {
            CartScreen(
                filteredProducts = uiState.filteredProducts,
                navController = navController,
                onOrder = {
                    viewModel.sendIntent(ProductIntent.SetSelectedProducts(it))
                    navController.navigate("commande")
                },
                isUserLoggedIn = isUserLoggedIn,
                cartCount = uiState.cartCount,
                searchTerm = uiState.searchTerm,
                onSearchChange = { viewModel.onSearchChange(it) },
                onSearchSubmit = { viewModel.onSearchSubmit() },
                onNavigateToOrders = {
                    if (isUserLoggedIn && userId != -1) {
                        navController.navigate("orders/$userId")
                    } else {
                        navController.navigate("login")
                    }
                },
                onNavigateToCart = { navController.navigate("cart") },
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToAccount = {
                    if (isUserLoggedIn && userId != -1) navController.navigate("account")
                    else navController.navigate("login")
                },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onLogout = {
                    prefs.edit().clear().apply()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("favorites") {
            FavoritesScreen(
                userId = userId,
                navController = navController,
                isUserLoggedIn = isUserLoggedIn,
                cartCount = uiState.cartCount,
                searchTerm = uiState.searchTerm,
                onSearchChange = { viewModel.onSearchChange(it) },
                onSearchSubmit = { viewModel.onSearchSubmit() },
                onNavigateToOrders = {
                    if (isUserLoggedIn && userId != -1) {
                        navController.navigate("orders/$userId")
                    } else {
                        navController.navigate("login")
                    }
                },
                onNavigateToCart = { navController.navigate("cart") },
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToAccount = {
                    if (isUserLoggedIn && userId != -1) navController.navigate("account")
                    else navController.navigate("login")
                },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onLogout = {
                    prefs.edit().clear().apply()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }


        composable("commande") {
            if (isUserLoggedIn && userId != -1) {
                CommandeScreen(
                    selectedProducts = uiState.selectedProducts,
                    onBack = { navController.popBackStack() },
                    onConfirm = {
                        navController.navigate("orders/${it.userId}")
                    },
                    navController = navController,

                    isUserLoggedIn = isUserLoggedIn,
                    cartCount = uiState.cartCount,
                    searchTerm = uiState.searchTerm,
                    onSearchChange = { viewModel.onSearchChange(it) },
                    onSearchSubmit = { viewModel.onSearchSubmit() },
                    onNavigateToOrders = {
                        if (isUserLoggedIn && userId != -1) {
                            navController.navigate("orders/$userId")
                        } else {
                            navController.navigate("login")
                        }
                    },
                    onNavigateToCart = { navController.navigate("cart") },
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToAccount = {
                        if (isUserLoggedIn && userId != -1) navController.navigate("account")
                        else navController.navigate("login")
                    },
                    onNavigateToFavorites = { navController.navigate("favorites") },
                    onLogout = {
                        prefs.edit().clear().apply()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) { navController.navigate("login") }
            }
        }

        composable("orders/{userId}", arguments = listOf(navArgument("userId") { type = NavType.IntType })) {
            if (isUserLoggedIn && userId != -1) {
                val uid = it.arguments?.getInt("userId") ?: -1
                OrderListScreen(
                    userId = uid,
                    onBack = { navController.popBackStack() },
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    cartCount = uiState.cartCount,
                    searchTerm = uiState.searchTerm,
                    onSearchChange = { viewModel.onSearchChange(it) },
                    onSearchSubmit = { viewModel.onSearchSubmit() },
                    onNavigateToOrders = {
                        if (isUserLoggedIn && userId != -1) {
                            navController.navigate("orders/$userId")
                        } else {
                            navController.navigate("login")
                        }
                    },
                    onNavigateToCart = { navController.navigate("cart") },
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToAccount = {
                        if (isUserLoggedIn && userId != -1) navController.navigate("account")
                        else navController.navigate("login")
                    },
                    onNavigateToFavorites = { navController.navigate("favorites") },
                    onLogout = {
                        prefs.edit().clear().apply()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) { navController.navigate("login") }
            }
        }

        composable("account") {
            if (isUserLoggedIn && userId != -1) {
                AccountScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToLogin = { navController.navigate("login") },
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    cartCount = uiState.cartCount,
                    searchTerm = uiState.searchTerm,
                    onSearchChange = { viewModel.onSearchChange(it) },
                    onSearchSubmit = { viewModel.onSearchSubmit() },
                    onNavigateToOrders = {
                        if (isUserLoggedIn && userId != -1) {
                            navController.navigate("orders/$userId")
                        } else {
                            navController.navigate("login")
                        }
                    },
                    onNavigateToCart = { navController.navigate("cart") },
                    onNavigateToAccount = {
                        if (isUserLoggedIn && userId != -1) navController.navigate("account")
                        else navController.navigate("login")
                    },
                    onNavigateToFavorites = { navController.navigate("favorites") },
                    onLogout = {
                        prefs.edit().clear().apply()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("login")
                }
            }
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }




    }
}
