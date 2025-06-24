package com.example.my_projet.data.Entities

data class CartItem(
    val userId: Int,
    val productId: String,
    val productName: String,
    val productPrice: String,
    val imageUrl: String,
    val dateAdded: String
)