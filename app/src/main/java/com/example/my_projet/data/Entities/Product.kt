package com.example.my_projet.data.Entities

import com.google.gson.annotations.SerializedName



    data class Product(
        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("description")
        val description: String? = null,

        @SerializedName("price")
        val price: Int ,

        @SerializedName("quantity")
        val quantity: Int? = null,

        @SerializedName("imageUrl")
        val imageUrl: String? = null,

        @SerializedName("author")
        val author: String? = null,

        @SerializedName("genre")
        val genre: String? = null,

        @SerializedName("rating")
        val rating: Double? = null,

        @SerializedName("chapters")
        val chapters: Int? = null,

        @SerializedName("fullSummary")
        val fullSummary: String? = null,

            )