package com.example.my_projet.ui.auth

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
import com.example.my_projet.data.Entities.User
import com.example.my_projet.data.Api.saveUser
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var registrationSuccess by remember { mutableStateOf(false) }

    val orangeColor = Color(0xFFFF6F00)

    // Redirection vers l’écran de login après succès
    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            delay(2000)
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Start
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
                stringResource(R.string.register_title),
                style = MaterialTheme.typography.titleLarge,
                color = orangeColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(stringResource(R.string.firstname_label), color = orangeColor) },
                modifier = Modifier.fillMaxWidth(),
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
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(R.string.lastname_label), color = orangeColor) },
                modifier = Modifier.fillMaxWidth(),
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
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_label), color = orangeColor) },
                modifier = Modifier.fillMaxWidth(),
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
                label = { Text(stringResource(R.string.password_label), color = orangeColor) },
                modifier = Modifier.fillMaxWidth(),
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
                    if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                        message = context.getString(R.string.fill_all_fields)
                        return@Button
                    }

                    val newUser = User(0, firstName, lastName, email, password)
                    val success = saveUser(context, newUser)

                    if (success) {
                        message = context.getString(R.string.register_success)
                        registrationSuccess = true
                    } else {
                        message = context.getString(R.string.email_already_exists)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = orangeColor)
            ) {
                Text(stringResource(R.string.signup_button), color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text(stringResource(R.string.already_have_account), color = orangeColor)
            }

            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    color = if (message.contains("réussi")) orangeColor else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
