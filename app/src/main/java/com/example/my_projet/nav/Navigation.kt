package com.example.my_projet.nav

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.my_projet.data.Api.readUsers
import com.example.my_projet.data.Entities.User
import com.example.my_projet.ui.auth.AccountScreen
import com.example.my_projet.ui.auth.LoginScreen
import com.example.my_projet.ui.auth.RegisterScreen
import com.example.my_projet.ui.card.CartScreen
import com.example.my_projet.ui.favorites.FavoritesScreen
import com.example.my_projet.ui.orders.CommandeScreen
import com.example.my_projet.ui.orders.OrderListScreen
import com.example.my_projet.ui.product.ProductIntent
import com.example.my_projet.ui.product.ProductViewModel
import com.example.my_projet.ui.product.component.DetailsScreen
import com.example.my_projet.ui.product.screens.*

object Routes {
    const val Home = "home"
    const val Login = "login"
    const val Register = "register"
    const val ProductDetails = "productDetails"
    const val Cart = "cart"
    const val Favorites = "favorites"
    const val Orders = "orders"
    const val Commande = "commande"
    const val Account = "account"
    const val EditProfile = "editProfile"
}

@Composable
fun AppNavigation(viewModel: ProductViewModel,onLanguageSelected: (String) -> Unit) {
    val context = LocalContext.current
    val navController = rememberNavController()

    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val isUserLoggedIn = prefs.getBoolean("logged_in", false)
    val userId = prefs.getInt("user_id", -1)

    val uiState by viewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = Routes.Home) {

        composable(Routes.Login) {
            LoginScreen(navController) { loggedUser ->
                prefs.edit().putBoolean("logged_in", true)
                    .putInt("user_id", loggedUser.id).apply()
                navController.navigate(Routes.Home) {
                    popUpTo(Routes.Login) { inclusive = true }
                }
            }
        }

        composable(Routes.Register) {
            RegisterScreen(navController)
        }

        composable(Routes.Home) {
            HomeScreen(
                searchTerm = uiState.searchTerm,
                onSearchChange = { viewModel.onSearchChange(it) },
                onSearchSubmit = { viewModel.onSearchSubmit() },
                cartCount = uiState.cartCount,
                onNavigateToOrders = {
                    if (userId != -1) navController.navigate("${Routes.Orders}/$userId")
                    else navController.navigate(Routes.Login)
                },
                onNavigateToCart = { navController.navigate(Routes.Cart) },
                genres = uiState.genres,
                selectedGenre = uiState.selectedGenre,
                topProducts = uiState.topProducts,
                filteredProducts = uiState.filteredProducts,
                activeTopIndex = uiState.activeTopIndex,
                onGenreSelected = { viewModel.sendIntent(ProductIntent.SelectGenre(it)) },
                onClearGenre = { viewModel.sendIntent(ProductIntent.ClearGenre) },
                onNavigateToDetails = { navController.navigate("${Routes.ProductDetails}/$it") },
                onPreviousTop = { viewModel.sendIntent(ProductIntent.PreviousTopProduct) },
                onNextTop = { viewModel.sendIntent(ProductIntent.NextTopProduct) },
                onAddToCart = { viewModel.sendIntent(ProductIntent.AddToCart(it)) },
                navController = navController,
                userId = userId,
                isUserLoggedIn = isUserLoggedIn,
                onNavigateToLogin = { navController.navigate(Routes.Login) },
                onNavigateToAccount = {
                    if (userId != -1) navController.navigate(Routes.Account)
                    else navController.navigate(Routes.Login)
                },
                onLogout = {
                    prefs.edit().clear().apply()
                    navController.navigate(Routes.Login) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable("${Routes.ProductDetails}/{productId}", arguments = listOf(navArgument("productId") { type = NavType.StringType })) {
            val productId = it.arguments?.getString("productId") ?: ""
            val product = uiState.products.find { it.id.toString() == productId }

            if (product != null) {
                DetailsScreen(
                    product = product,
                    searchTerm = uiState.searchTerm,
                    onSearchChange = { viewModel.onSearchChange(it) },
                    onSearchSubmit = { viewModel.onSearchSubmit() },
                    cartCount = uiState.cartCount,
                    onNavigateToOrders = {
                        if (userId != -1) navController.navigate("${Routes.Orders}/$userId")
                        else navController.navigate(Routes.Login)
                    },
                    onNavigateToCart = { navController.navigate(Routes.Cart) },
                    navController = navController,
                    userId = userId,
                    isUserLoggedIn = isUserLoggedIn,
                    onNavigateToLogin = { navController.navigate(Routes.Login) },
                    onNavigateToAccount = {
                        if (userId != -1) navController.navigate(Routes.Account)
                        else navController.navigate(Routes.Login)
                    },
                    onLogout = {
                        prefs.edit().clear().apply()
                        navController.navigate(Routes.Login) { popUpTo(0) { inclusive = true } }
                    }
                )
            }
        }

        composable(Routes.Cart) {
            CartScreen(
                filteredProducts = uiState.filteredProducts,
                navController = navController,
                onOrder = {
                    viewModel.sendIntent(ProductIntent.SetSelectedProducts(it))
                    navController.navigate(Routes.Commande)
                },
                isUserLoggedIn = isUserLoggedIn,
                cartCount = uiState.cartCount,
                searchTerm = uiState.searchTerm,
                onSearchChange = { viewModel.onSearchChange(it) },
                onSearchSubmit = { viewModel.onSearchSubmit() },
                onNavigateToOrders = {
                    if (userId != -1) navController.navigate("${Routes.Orders}/$userId")
                    else navController.navigate(Routes.Login)
                },
                onNavigateToCart = { navController.navigate(Routes.Cart) },
                onNavigateToLogin = { navController.navigate(Routes.Login) },
                onNavigateToAccount = {
                    if (userId != -1) navController.navigate(Routes.Account)
                    else navController.navigate(Routes.Login)
                },
                onNavigateToFavorites = { navController.navigate(Routes.Favorites) },
                onLogout = {
                    prefs.edit().clear().apply()
                    navController.navigate(Routes.Login) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(Routes.Favorites) {
            FavoritesScreen(
                userId = userId,
                navController = navController,
                isUserLoggedIn = isUserLoggedIn,
                cartCount = uiState.cartCount,
                searchTerm = uiState.searchTerm,
                onSearchChange = { viewModel.onSearchChange(it) },
                onSearchSubmit = { viewModel.onSearchSubmit() },
                onNavigateToOrders = {
                    if (userId != -1) navController.navigate("${Routes.Orders}/$userId")
                    else navController.navigate(Routes.Login)
                },
                onNavigateToCart = { navController.navigate(Routes.Cart) },
                onNavigateToLogin = { navController.navigate(Routes.Login) },
                onNavigateToAccount = {
                    if (userId != -1) navController.navigate(Routes.Account)
                    else navController.navigate(Routes.Login)
                },
                onNavigateToFavorites = { navController.navigate(Routes.Favorites) },
                onLogout = {
                    prefs.edit().clear().apply()
                    navController.navigate(Routes.Login) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(Routes.Commande) {
            if (userId != -1) {
                CommandeScreen(
                    selectedProducts = uiState.selectedProducts,
                    onBack = { navController.popBackStack() },
                    onConfirm = { navController.navigate("${Routes.Orders}/${it.userId}") },
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    cartCount = uiState.cartCount,
                    searchTerm = uiState.searchTerm,
                    onSearchChange = { viewModel.onSearchChange(it) },
                    onSearchSubmit = { viewModel.onSearchSubmit() },
                    onNavigateToOrders = {
                        if (userId != -1) navController.navigate("${Routes.Orders}/$userId")
                        else navController.navigate(Routes.Login)
                    },
                    onNavigateToCart = { navController.navigate(Routes.Cart) },
                    onNavigateToLogin = { navController.navigate(Routes.Login) },
                    onNavigateToAccount = {
                        if (userId != -1) navController.navigate(Routes.Account)
                        else navController.navigate(Routes.Login)
                    },
                    onNavigateToFavorites = { navController.navigate(Routes.Favorites) },
                    onLogout = {
                        prefs.edit().clear().apply()
                        navController.navigate(Routes.Login) { popUpTo(0) { inclusive = true } }
                    }
                )
            } else LaunchedEffect(Unit) { navController.navigate(Routes.Login) }
        }

        composable("${Routes.Orders}/{userId}", arguments = listOf(navArgument("userId") { type = NavType.IntType })) {
            val uid = it.arguments?.getInt("userId") ?: -1
            if (uid != -1) {
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
                        if (userId != -1) navController.navigate("${Routes.Orders}/$userId")
                        else navController.navigate(Routes.Login)
                    },
                    onNavigateToCart = { navController.navigate(Routes.Cart) },
                    onNavigateToLogin = { navController.navigate(Routes.Login) },
                    onNavigateToAccount = {
                        if (userId != -1) navController.navigate(Routes.Account)
                        else navController.navigate(Routes.Login)
                    },
                    onNavigateToFavorites = { navController.navigate(Routes.Favorites) },
                    onLogout = {
                        prefs.edit().clear().apply()
                        navController.navigate(Routes.Login) { popUpTo(0) { inclusive = true } }
                    }
                )
            }
        }

        composable(Routes.Account) {
            var user by remember { mutableStateOf<User?>(null) }
            var isChecking by remember { mutableStateOf(true) }
            var favoritesCount by remember { mutableStateOf(0) }
            var ordersCount by remember { mutableStateOf(0) }

            LaunchedEffect(Unit) {
                if (userId == -1) {
                    navController.navigate(Routes.Login)
                } else {
                    val allUsers = readUsers(context)
                    user = allUsers.find { it.id == userId }
                    favoritesCount = 2
                    ordersCount = 3
                }
                isChecking = false
            }

            if (isChecking) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                user?.let {
                    AccountScreen(
                        user = it,
                        favoritesCount = favoritesCount,
                        ordersCount = ordersCount,
                        cartCount = uiState.cartCount,
                        onLogout = {
                            prefs.edit().clear().apply()
                            navController.navigate(Routes.Login) { popUpTo(0) { inclusive = true } }
                        },
                        onBack = { navController.popBackStack() },

                        onEditProfile = { navController.navigate(Routes.EditProfile) },
                        onLanguageSelected = onLanguageSelected
                    )
                } ?: LaunchedEffect(Unit) {
                    navController.navigate(Routes.Login)
                }
            }
        }
    }
}
