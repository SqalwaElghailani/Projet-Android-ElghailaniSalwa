package com.example.my_projet.ui.product.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.my_projet.data.Api.readUsers
import com.example.my_projet.data.Entities.User

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (User) -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Se connecter", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Mot de passe") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val users = readUsers(context)
            val foundUser = users.find { it.email == email && it.password == password }
            if (foundUser != null) {
                val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                prefs.edit()
                    .putBoolean("logged_in", true)
                    .putInt("user_id", foundUser.id)
                    .apply()

                onLoginSuccess(foundUser)
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                message = "Email ou mot de passe incorrect"
            }
        }) {
            Text("Connexion")
        }

        if (message.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lien vers la page d'inscription
        TextButton(onClick = {
            navController.navigate("register")
        }) {
            Text("Pas de compte ? Inscrivez-vous ici")
        }
    }
}
