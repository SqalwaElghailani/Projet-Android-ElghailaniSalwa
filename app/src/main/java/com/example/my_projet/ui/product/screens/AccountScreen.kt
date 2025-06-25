package com.example.my_projet.ui.product.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.my_projet.data.Api.readUsers
import com.example.my_projet.data.Entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AccountScreen(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    var user by remember { mutableStateOf<User?>(null) }
    var isChecking by remember { mutableStateOf(true) }

    // ✅ جلب بيانات المستخدم من SharedPreferences ثم ملف users.json
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        if (userId == -1) {
            onNavigateToLogin()
        } else {
            // ✅ نحاول نقرأ المستخدم من JSON
            val allUsers = withContext(Dispatchers.IO) { readUsers(context) }
            user = allUsers.find { it.id == userId }

            if (user == null) {
                onNavigateToLogin()
            }
        }
        isChecking = false
    }

    if (isChecking) {
        // في حالة الانتظار
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
