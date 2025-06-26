package com.example.my_projet.ui.product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*


@Composable
fun GenresList(
    genres: List<String>,
    selectedGenre: String?,
    onGenreSelected: (String) -> Unit,
    onClearGenre: () -> Unit
) {
    var startIndex by remember { mutableStateOf(0) }

    // دائما "Tous" فالأول (ماكيحسبش مع rotate)
    val visibleGenres = if (selectedGenre == null || selectedGenre == "Tous") {
        // كيظهر Tous + أول 3 genres
        genres.take(3)
    } else {
        // كيظهرو 3 genres ابتداء من startIndex
        List(3) { i ->
            genres[(startIndex + i) % genres.size]
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // عرض Tous دايما فالأول
        val isTousSelected = selectedGenre == null || selectedGenre == "Tous"
        Box(
            modifier = Modifier
                .background(
                    color = if (isTousSelected) Color(0xFFFF9800) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    onClearGenre()
                    startIndex = 0
                }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Tous",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        // عرض باقي genres
        visibleGenres.forEach { genre ->
            val isSelected = genre == selectedGenre
            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) Color(0xFFFF9800) else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        onGenreSelected(genre)
                        val indexInGenres = genres.indexOf(genre)
                        startIndex = indexInGenres
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = genre,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


