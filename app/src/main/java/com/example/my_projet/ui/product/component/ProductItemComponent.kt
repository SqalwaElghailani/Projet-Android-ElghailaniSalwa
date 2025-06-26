package com.example.my_projet.ui.product.component

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.my_projet.R
import com.example.my_projet.data.Api.CardApi
import com.example.my_projet.data.Entities.CartItem
import com.example.my_projet.data.Entities.Product
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.layout.ContentScale

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
                        .align(androidx.compose.ui.Alignment.BottomEnd)
                        .padding(4.dp)

                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    color = androidx.compose.ui.graphics.Color.White,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )

            }

                Text(
                    text = "${product.name}",
                    modifier = Modifier.padding(top = 4.dp)
                )


                androidx.compose.foundation.layout.Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
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
                                val sdf =
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                val currentDate = sdf.format(Date())
                                val item = CartItem(
                                    userId = userId,
                                    productId = product.id.toString(),
                                    productName = product.name.toString(),
                                    productPrice = product.price.toString(),
                                    imageUrl = product.imageUrl.toString(),
                                    dateAdded = currentDate
                                )
                                CardApi(context, item)
                            }
                        },
                        modifier = Modifier.height(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = androidx.compose.ui.graphics.Color(0xFFFF9800),
                            contentColor = androidx.compose.ui.graphics.Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ajouter au panier"
                        )
                    }


                    Text(
                        text = "${product.price} MAD",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    }
