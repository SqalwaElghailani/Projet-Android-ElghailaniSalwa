package com.example.my_projet.nav

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.my_projet.ui.product.ProductIntent
import com.example.my_projet.ui.product.ProductViewModel
import com.example.my_projet.ui.product.component.DetailsScreen
import com.example.my_projet.ui.product.screens.HomeScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

object Routes {
    const val Home = "home"
    const val ProductDetails = "productDetails"
}

@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    val uiState by viewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = Routes.Home) {

        composable(Routes.Home) {
            HomeScreen(
                searchTerm = uiState.searchTerm,
                onSearchChange = { newTerm -> viewModel.onSearchChange(newTerm) },
                onSearchSubmit = { viewModel.onSearchSubmit() },
                cartCount = uiState.cartCount,
                onNavigateToOrders = {
                    // Par exemple navigation vers l'écran des commandes
                    // navController.navigate("orders")
                },
                onNavigateToCart = {
                    // Par exemple navigation vers le panier
                    // navController.navigate("cart")
                },
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
                onAddToCart = { product -> viewModel.sendIntent(ProductIntent.AddToCart(product)) }
            )
        }

        composable(
            "${Routes.ProductDetails}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            DetailsScreen(id = productId)
        }
    }
}
