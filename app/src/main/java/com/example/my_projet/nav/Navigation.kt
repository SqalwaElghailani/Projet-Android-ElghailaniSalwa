    package com.example.my_projet.nav

    import android.util.Log
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

    object Routes {
        const val Home = "home"
        const val ProductDetails = "productDetails"
    }

    @Composable
    fun AppNavigation(viewModel: ProductViewModel, userId: Int = 1) {
        var navController = rememberNavController()

        val uiState by viewModel.state.collectAsState()

        NavHost(navController = navController, startDestination = Routes.Home) {

            composable(Routes.Home) {
                HomeScreen(
                    searchTerm = uiState.searchTerm,
                    onSearchChange = { newTerm -> viewModel.onSearchChange(newTerm) },
                    onSearchSubmit = { viewModel.onSearchSubmit() },
                    cartCount = uiState.cartCount,
                    onNavigateToOrders = {
                        navController.navigate("orders/$userId")
                    },
                    onNavigateToCart = {
                        Log.d("NAVIGATION", "Navigating to cart")
                        navController.navigate("cart")
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
                    onAddToCart = { product -> viewModel.sendIntent(ProductIntent.AddToCart(product)) },navController = navController,userId = userId

                )
            }

            composable(
                "${Routes.ProductDetails}/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                DetailsScreen(id = productId)
            }
            composable("cart") {
                CartScreen(
                    filteredProducts = uiState.filteredProducts,
                    onBack = { navController.popBackStack() },
                    onOrder = { selectedProducts ->
                        // نسجلو فـ viewModel باش نسترجعوهم فـ commande
                        viewModel.sendIntent(ProductIntent.SetSelectedProducts(selectedProducts))
                        navController.navigate("commande")
                    }
                )
            }

            composable("commande") {
                CommandeScreen(
                    selectedProducts = uiState.selectedProducts,
                    onBack = { navController.popBackStack() },
                    onConfirm = { order ->
                        println("Commande confirmée: $order")
                        navController.navigate("orders/${order.userId}")
                    },
                    navController = navController
                )
            }

            composable(
                "orders/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: 1
                println("Cccccccccccccccccccc: ")

                OrderListScreen(
                    userId = userId,
                    onBack = { navController.popBackStack() },
                    navController = navController

                )
            }

        }




        }

