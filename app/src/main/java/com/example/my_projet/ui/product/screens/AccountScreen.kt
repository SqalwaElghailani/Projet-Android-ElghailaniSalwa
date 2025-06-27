package com.example.my_projet.ui.product.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.my_projet.R
import com.example.my_projet.data.Entities.User

@Composable
fun AccountScreen(
    user: User,
    favoritesCount: Int,
    ordersCount: Int,
    cartCount: Int,
    onLogout: () -> Unit,
    onEditProfile: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 4.dp, end = 4.dp, top = 20.dp, bottom = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            // خلفية الصورة
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // زر الرجوع
            IconButton(
                onClick = { onBack() },
                modifier = Modifier.align(Alignment.TopStart).padding(12.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = Color.Black)
            }

            // زر 3 نقاط وزر الخروج جنب بعض في الأعلى على اليمين
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { onLogout() }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.Black)
                }
                IconButton(onClick = { /* هنا دير شي أكشن ديال 3 نقاط إذا بغيت */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(-40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // صورة المستخدم
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "User Logo",
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, shape = CircleShape)
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // الاسم تحت لوجو
            Text(
                text = user.firstName + " " + user.lastName,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            // الإيميل تحت الاسم
            Text(
                text = user.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // زر تعديل الملف تحت الإيميل
            OutlinedButton(onClick = onEditProfile) {
                Text("Modifier le profil")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Divider()

//        // الإحصائيات (معلقة كما طلبت)
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            StatItem(label = "Commandes", value = ordersCount)
//            StatItem(label = "Favoris", value = favoritesCount)
//            StatItem(label = "Panier", value = cartCount)
//        }
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value.toString(), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
    }
}
