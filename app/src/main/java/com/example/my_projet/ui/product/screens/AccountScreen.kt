package com.example.my_projet.ui.product.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.my_projet.data.Api.readUsers
import com.example.my_projet.data.Entities.User
import com.example.my_projet.ui.product.component.MainHeader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AccountScreen(
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
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var user by remember { mutableStateOf<User?>(null) }
    var isChecking by remember { mutableStateOf(true) }
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = prefs.getInt("user_id", -1)

    LaunchedEffect(Unit) {
        if (userId == -1) {
            onNavigateToLogin()
        } else {
            val allUsers = withContext(Dispatchers.IO) { readUsers(context) }
            user = allUsers.find { it.id == userId }
            if (user == null) {
                onNavigateToLogin()
            }
        }
        isChecking = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, end = 4.dp, top = 20.dp, bottom = 20.dp)
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
            onNavigateToFavorites = onNavigateToFavorites,
            userId = userId,
            onNavigateToHome = { navController.navigate("home") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (isChecking) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            user?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Bienvenue, ${it.firstName} ✨", style = MaterialTheme.typography.headlineSmall)
                    Text("Email: ${it.email}", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = onBack) {
                        Text("Retour")
                    }
                }
            }
        }
    }
}
