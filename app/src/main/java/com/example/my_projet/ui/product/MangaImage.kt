package com.example.my_projet.ui.product

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.my_projet.R

@Composable
fun MangaImage(mangaImageName: String) {
    val context = LocalContext.current

    val imageResId = context.resources.getIdentifier(
        mangaImageName, "drawable", context.packageName
    )

    if (imageResId != 0) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ml),
            contentDescription = "Image par défaut"
        )
    }
}
