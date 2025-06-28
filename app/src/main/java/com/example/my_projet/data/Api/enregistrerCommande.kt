package com.example.my_projet.data.Api

import android.content.Context
import android.util.Log
import com.example.my_projet.ui.orders.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

fun enregistrerCommande(context: Context, commande: Order) {
    val gson = Gson()
    val file = File(context.filesDir, "commandes.json")

    val commandesExistantes: MutableList<Order> = if (file.exists()) {
        val json = file.readText()
        if (json.isNotBlank()) {
            val type = object : TypeToken<MutableList<Order>>() {}.type
            gson.fromJson(json, type)
        } else mutableListOf()
    } else mutableListOf()

    // Générer automatiquement un nouvel ID basé sur le plus grand existant
    val newId = (commandesExistantes.maxOfOrNull { it.id } ?: 0) + 1
    val commandeAvecId = commande.copy(id = newId)

    commandesExistantes.add(commandeAvecId)

    val jsonFinal = gson.toJson(commandesExistantes)
    file.writeText(jsonFinal)

    Log.d("Commande", "Commande enregistrée avec ID: $newId")
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
fun supprimerCommandeParId(context: Context, orderId: Int) {
    val gson = Gson()
    val file = File(context.filesDir, "commandes.json")

    if (file.exists()) {
        val json = file.readText()
        val type = object : TypeToken<MutableList<Order>>() {}.type
        val commandes: MutableList<Order> = gson.fromJson(json, type)

        val nouvellesCommandes = commandes.filter { it.id != orderId }
        file.writeText(gson.toJson(nouvellesCommandes))

        Log.d("SupprimerCommande", "Commande avec ID $orderId supprimée.")
    } else {
        Log.w("SupprimerCommande", "Fichier commandes.json non trouvé.")
    }
}
