package com.example.my_projet.data.Api

import android.content.Context
import android.util.Log
import com.example.my_projet.data.Entities.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

fun CardApi(context: Context, item: CartItem) {
    Log.d("CardApi", "==> Entered CardApi function")

    val file = File(context.filesDir, "cart.json")
    val gson = Gson()
    val itemType = object : TypeToken<MutableList<CartItem>>() {}.type

    Log.d("CardApi", "Checking if file exists...")
    val existingItems: MutableList<CartItem> = if (file.exists()) {
        try {
            val json = file.readText()
            Log.d("CardApi", "Existing JSON content: $json")
            gson.fromJson(json, itemType) ?: mutableListOf()
        } catch (e: Exception) {
            Log.e("CardApi", "Error parsing JSON: ${e.message}")
            mutableListOf()
        }
    } else {
        Log.d("CardApi", "File does not exist. Creating new list.")
        mutableListOf()
    }

    val alreadyExists = existingItems.any {
        it.userId == item.userId && it.productId == item.productId
    }

    if (!alreadyExists) {
        existingItems.add(item)
        file.writeText(gson.toJson(existingItems))
        Log.d("CardApi", "File saved with ${existingItems.size} items")
    } else {
        Log.d("CardApi", "Item already exists, not adding")
    }
    Log.d("CardApi", "All content: ${file.readText()}")

}

