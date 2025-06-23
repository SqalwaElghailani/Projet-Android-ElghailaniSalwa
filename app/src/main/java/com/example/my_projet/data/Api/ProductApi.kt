package com.example.my_projet.data.Api

import com.example.my_projet.data.Entities.Product
import retrofit2.http.GET

interface ProductApi {
    @GET("products.json")
    suspend fun getProducts(): List<Product>
}