package com.example.my_projet.ui.product.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    val orangeColor = Color(0xFFFF6F00)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Retour",
                    tint = orangeColor
                )
            }

//            IconButton(onClick = { /* Logout Action */ }) {
//                Icon(
//                    imageVector = Icons.Default.ExitToApp,
//                    contentDescription = "Logout",
//                    tint = orangeColor
//                )
//            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Se connecter",
                style = MaterialTheme.typography.titleLarge,
                color = orangeColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = orangeColor) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = orangeColor,
                    unfocusedBorderColor = orangeColor,
                    cursorColor = orangeColor,
                    focusedLabelColor = orangeColor,
                    focusedTextColor = orangeColor,
                    unfocusedTextColor = orangeColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe", color = orangeColor) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = orangeColor,
                    unfocusedBorderColor = orangeColor,
                    cursorColor = orangeColor,
                    focusedLabelColor = orangeColor,
                    focusedTextColor = orangeColor,
                    unfocusedTextColor = orangeColor
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
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
                },
                colors = ButtonDefaults.buttonColors(containerColor = orangeColor)
            ) {
                Text("Connexion", color = Color.White)
            }

            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(message, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = {
                    navController.navigate("register")
                }
            ) {
                Text("Pas de compte ? Inscrivez-vous ici", color = orangeColor)
            }
        }
    }
}
