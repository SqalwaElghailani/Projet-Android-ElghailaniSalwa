package com.example.my_projet.data.Api

import android.content.Context
import android.util.Log
import com.example.my_projet.data.Entities.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

fun addToJaimeApi(context: Context, item: CartItem) {
    val file = File(context.filesDir, "jaime.json")
    val gson = Gson()
    val type = object : TypeToken<MutableList<CartItem>>() {}.type

    val existingItems: MutableList<CartItem> = if (file.exists()) {
        try {
            val json = file.readText()
            gson.fromJson(json, type) ?: mutableListOf()
        } catch (e: Exception) {
            Log.e("JaimeApi", "Erreur lecture JSON: ${e.message}")
            mutableListOf()
        }
    } else mutableListOf()

    val alreadyExists = existingItems.any {
        it.userId == item.userId && it.productId == item.productId
    }

    if (!alreadyExists) {
        existingItems.add(item)
        file.writeText(gson.toJson(existingItems))
        Log.d("JaimeApi", "Ajouté avec succès à jaime.json")
    } else {
        Log.d("JaimeApi", "Produit déjà existant dans j’aime")
    }
}

fun removeFromJaimeApi(context: Context, productId: String, userId: Int) {
    val file = File(context.filesDir, "jaime.json")
    val gson = Gson()
    val type = object : TypeToken<MutableList<CartItem>>() {}.type

    if (!file.exists()) return

    try {
        val items: MutableList<CartItem> =
            gson.fromJson(file.readText(), type) ?: mutableListOf()

        val updatedItems = items.filterNot {
            it.productId == productId && it.userId == userId
        }

        file.writeText(gson.toJson(updatedItems))
        Log.d("JaimeApi", "Supprimé avec succès de jaime.json")
    } catch (e: Exception) {
        Log.e("JaimeApi", "Erreur suppression: ${e.message}")
    }
}

fun readJaimeItems(context: Context): List<CartItem> {
    val file = File(context.filesDir, "jaime.json")
    val gson = Gson()
    val type = object : TypeToken<List<CartItem>>() {}.type

    return if (file.exists()) {
        try {
            val json = file.readText()
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            Log.e("JaimeApi", "Erreur lecture: ${e.message}")
            emptyList()
        }
    } else emptyList()
}
