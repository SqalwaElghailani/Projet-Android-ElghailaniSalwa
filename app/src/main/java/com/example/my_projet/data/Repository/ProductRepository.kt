package com.example.my_projet.data.Repository
import android.util.Log
import com.example.my_projet.data.Api.ProductApi

import com.example.my_projet.data.Entities.Product
import javax.inject.Inject


open class ProductRepository @Inject constructor(
    private val api: ProductApi
) {
    suspend fun getProducts(): List<Product> {
        // fetch data from a remote server
        val products = api.getProducts()
        Log.d("products repo", "size :"+ products.size)
        return products
    }
}