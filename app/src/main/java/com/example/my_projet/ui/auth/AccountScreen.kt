package com.example.my_projet.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    onLanguageSelected: (String) -> Unit // "ar", "fr"
) {
    val showMenu = remember { mutableStateOf(false) }

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
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = { onBack() },
                modifier = Modifier.align(Alignment.TopStart).padding(12.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = Color.Black
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onLogout) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = stringResource(id = R.string.logout),
                        tint = Color.Black
                    )
                }

                Box {
                    IconButton(onClick = { showMenu.value = true }) {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = stringResource(id = R.string.language),
                            tint = Color.Black
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu.value,
                        onDismissRequest = { showMenu.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.lang_arabic)) },
                            onClick = {
                                showMenu.value = false
                                onLanguageSelected("ar")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.lang_french)) },
                            onClick = {
                                showMenu.value = false
                                onLanguageSelected("fr")
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(-40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = stringResource(id = R.string.user_logo_desc),
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, shape = CircleShape)
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${user.firstName} ${user.lastName}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = user.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = onEditProfile) {
                Text(text = stringResource(id = R.string.modify_profile))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Divider()
    }
}
