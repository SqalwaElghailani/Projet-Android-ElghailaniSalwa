package com.example.my_projet.ui.product.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.my_projet.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.layout.ContentScale

import com.example.my_projet.data.Entities.Product

@Composable
fun TopBannerWithGenres(
    product: Product,
    genres: List<String>,
    selectedGenre: String?,
    onGenreSelected: (String) -> Unit,
    onClearGenre: () -> Unit,
    onPreviousTop: () -> Unit,
    onNextTop: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        val context = LocalContext.current
        val imageResId = context.resources.getIdentifier(
            product.imageUrl, "drawable", context.packageName
        )

        Image(
            painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.ml),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ✅ لائحة الأصناف
//            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                genres.forEach { genre ->
//                    Text(
//                        text = genre,
//                        color = if (genre == selectedGenre) Color.Yellow else Color.White,
//                        modifier = Modifier.clickable { onGenreSelected(genre) }
//                    )
//                }
//
//                if (selectedGenre != null) {
//                    Text(
//                        text = "Tous",
//                        color = Color.Cyan,
//                        modifier = Modifier.clickable { onClearGenre() }
//                    )
//                }
//            }
            GenresList(
                genres = genres,
                selectedGenre = selectedGenre,
                onGenreSelected = onGenreSelected,
                onClearGenre = onClearGenre
            )
            // ✅ المعلومات + الأزرار
            Column {
                Text(text = " ${product.name}", color = Color.White, fontSize = 20.sp)
                Text(text = " ${product.genre}", color = Color.White)
                Text(text = " ${product.description}", color = Color.White, maxLines = 2)
                Text(
                    text = "${product.price} €",
                    color = Color(0xFFFF6B00),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onPreviousTop,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B00), // orange
                            contentColor = Color.White
                        )
                    ) {
                        Text("⬅ Précédent")
                    }
                    Button(
                        onClick = onNextTop,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B00), // orange
                            contentColor = Color.White
                        )
                    ) {
                        Text("Suivant ➡")
                    }
                }
            }
        }
    }
}
