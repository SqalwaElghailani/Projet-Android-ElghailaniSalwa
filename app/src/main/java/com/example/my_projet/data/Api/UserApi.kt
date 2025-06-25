package com.example.my_projet.data.Api
import android.content.Context
import android.util.Log
import com.example.my_projet.data.Entities.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
fun readUsers(context: Context): MutableList<User> {
    val file = File(context.filesDir, "users.json")
    if (!file.exists()) {
        Log.d("READ_USER", "File not found.")
        return mutableListOf()
    }

    val json = file.readText()
    return if (json.isNotBlank()) {
        try {
            val type = object : TypeToken<MutableList<User>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            Log.e("READ_USER", "Error parsing JSON: ${e.message}")
            mutableListOf()
        }
    } else {
        Log.d("READ_USER", "File is blank.")
        mutableListOf()
    }
}

fun saveUser(context: Context, newUser: User): Boolean {
    val users = readUsers(context)

    // Vérifier si l'email existe déjà
    if (users.any { it.email == newUser.email }) return false

    // Générer nouvel ID
    val newId = (users.maxOfOrNull { it.id } ?: 0) + 1
    val userToSave = newUser.copy(id = newId)

    users.add(userToSave)

    val file = File(context.filesDir, "users.json")
    file.writeText(Gson().toJson(users))
    Log.d("SAVE_USER", "users.json path: ${file.absolutePath}")
    Log.d("SAVE_USER", "Saved users: ${Gson().toJson(users)}")

    return true
}
