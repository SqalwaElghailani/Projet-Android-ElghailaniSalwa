package com.example.my_projet.ui.product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column

@Composable
fun GenresList(
    genres: List<String>,
    selectedGenre: String?,
    onGenreSelected: (String) -> Unit,
    onClearGenre: () -> Unit

) {
    Column {
        // N9sim la liste dyal genres f groupes dyal 3
        val chunkedGenres = genres.chunked(5)

        chunkedGenres.forEach { genreChunk ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 4.dp)) {
                genreChunk.forEach { genre ->
                    Text(
                        text = genre,
                        color = if (genre == selectedGenre) Color.Yellow else Color.White,
                        modifier = Modifier.clickable { onGenreSelected(genre) }
                    )
                }
            }
        }

        // Bouton pour "Tous" si genre sélectionné
//        if (selectedGenre != null) {
//            Text(
//                text = "Tous",
//                color = Color.Cyan,
//                modifier = Modifier
//                    .padding(top = 8.dp)
//                    .clickable { onClearGenre() }
//            )
//        }
    }
}

