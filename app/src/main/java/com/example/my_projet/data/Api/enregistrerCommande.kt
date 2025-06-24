package com.example.my_projet.data.Api

import android.content.Context
import android.util.Log
import com.example.my_projet.ui.product.screens.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

fun enregistrerCommande(context: Context, commande: Order) {
    val gson = Gson()
    val file = File(context.filesDir, "commandes.json")

    // Lire les commandes existantes
    val commandesExistantes: MutableList<Order> = if (file.exists()) {
        val json = file.readText()
        if (json.isNotBlank()) {
            val type = object : TypeToken<MutableList<Order>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    } else {
        mutableListOf()
    }

    // Ajouter la nouvelle commande
    commandesExistantes.add(commande)

    // Réécrire le fichier
    val jsonFinal = gson.toJson(commandesExistantes)
    file.writeText(jsonFinal)
}
fun lireCommandesParUser(context: Context, userId: Int): List<Order> {
    val gson = Gson()
    val file = File(context.filesDir, "commandes.json")

    return if (file.exists()) {
        try {
            val json = file.readText()
            val type = object : TypeToken<List<Order>>() {}.type
            val allOrders: List<Order> = gson.fromJson(json, type)

            // نرجعو غير الطلبات اللي عندهم نفس الuserId
            allOrders.filter { it.userId == userId }
        } catch (e: Exception) {
            Log.e("API", "Erreur lecture commandes par user: ${e.message}")
            emptyList()
        }
    } else {
        emptyList()
    }
}
