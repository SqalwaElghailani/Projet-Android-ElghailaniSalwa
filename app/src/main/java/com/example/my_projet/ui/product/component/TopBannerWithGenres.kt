package com.example.my_projet.ui.product.component
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.my_projet.R
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
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ✅ لائحة الأصناف
            GenresList(
                genres = genres,
                selectedGenre = selectedGenre,
                onGenreSelected = onGenreSelected,
                onClearGenre = onClearGenre
            )

            // ✅ المعلومات
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "${product.name}", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "${product.genre}", color = Color.LightGray)
                Text(text = "${product.description}", color = Color.White, maxLines = 2)
                Text(
                    text = "${product.price} €",
                    color = Color(0xFFFF6B00),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )

                // ✅ أزرار التنقل
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onPreviousTop,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
//                        border = BorderStroke(2.dp, Color(0xFFFF9800)),
//                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Précédent")
                    }

                    OutlinedButton(
                        onClick = onNextTop,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
//                        border = BorderStroke(2.dp, Color(0xFFFF9800)),
//                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Suivant")
                    }
                }
            }
        }
    }
}
