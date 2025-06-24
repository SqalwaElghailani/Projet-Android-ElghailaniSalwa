package com.example.my_projet.ui.product.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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

@Composable
fun ProductItem(
    product: Product,
    onNavigateToDetails: (String) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    val context = LocalContext.current

    val imageResId = context.resources.getIdentifier(
        product.imageUrl, "drawable", context.packageName
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Image clickable for details
            Image(
                painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.ml),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clickable { onNavigateToDetails(product.id.toString()) }, // <-- ici
            )

            Text(text = " ${product.name}")
            Text(text = " ${product.price}")
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDate = sdf.format(Date())

            val item = CartItem(
                userId = "userId",
                productId = product.id.toString(),
                productName = product.name.toString(),
                productPrice = product.price.toString(),
                imageUrl = product.imageUrl.toString(),
                dateAdded = currentDate
            )
            // Button for Add to Cart
            Button(onClick = { CardApi(context, item) }) {
                Text(text = "Ajouter au panier")

            }
        }
    }




}
