package com.example.my_projet.ui.product.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import com.example.my_projet.R
import com.example.my_projet.data.Api.CardApi
import com.example.my_projet.data.Entities.CartItem
import com.example.my_projet.data.Entities.Product
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProductItem(
    product: Product,
    onNavigateToDetails: (String) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = prefs.getInt("user_id", -1)

    val imageResId = context.resources.getIdentifier(
        product.imageUrl, "drawable", context.packageName
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable { onNavigateToDetails(product.id.toString()) }
            ) {
                Image(
                    painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.ml),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "${product.chapters} chapitres",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            }

            Text(
                text = product.name ?: "",
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (userId == -1) {
                            Toast.makeText(
                                context,
                                "Veuillez vous connecter pour ajouter au panier.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val currentDate = sdf.format(Date())
                            val item = CartItem(
                                userId = userId,
                                productId = product.id.toString(),
                                productName = product.name ?: "",
                                productPrice = product.price.toString(),
                                imageUrl = product.imageUrl ?: "",
                                dateAdded = currentDate
                            )
                            CardApi(context, item)
                            onAddToCart(product)  // ممكن تستدعي callback
                        }
                    },
                    modifier = Modifier.height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Ajouter au panier"
                    )
                }

                Text(
                    text = "${product.price} MAD",
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
