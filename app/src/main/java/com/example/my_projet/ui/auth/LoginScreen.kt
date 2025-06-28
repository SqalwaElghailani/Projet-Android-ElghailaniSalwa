package com.example.my_projet.ui.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.my_projet.R
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
                    contentDescription = stringResource(R.string.back_button_desc),
                    tint = orangeColor
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.login_title),
                style = MaterialTheme.typography.titleLarge,
                color = orangeColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(id = R.string.email_label), color = orangeColor) },
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
                label = { Text(stringResource(id = R.string.password_label), color = orangeColor) },
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
                        message = context.getString(R.string.login_error)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = orangeColor)
            ) {
                Text(stringResource(id = R.string.login_button), color = Color.White)
            }

            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(message, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = {
                navController.navigate("register")
            }) {
                Text(stringResource(id = R.string.no_account), color = orangeColor)
            }
        }
    }
}
