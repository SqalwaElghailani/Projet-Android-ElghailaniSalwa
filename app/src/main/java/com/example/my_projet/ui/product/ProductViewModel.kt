package com.example.my_projet.ui.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_projet.data.Repository.ProductRepository
import com.example.my_projet.data.Entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductViewState())
    val state: StateFlow<ProductViewState> = _state

    private val _intentChannel = Channel<ProductIntent>(Channel.UNLIMITED)
    val intentChannel = _intentChannel.receiveAsFlow()

    init {
        // Charger les produits dès l'initialisation
        sendIntent(ProductIntent.LoadProducts)

        // Lancer l'observateur d'intents
        handleIntents()
    }

    fun sendIntent(intent: ProductIntent) {
        viewModelScope.launch {
            _intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.collect { intent ->
                when (intent) {
                    is ProductIntent.LoadProducts -> loadProducts()
                    is ProductIntent.SelectGenre -> {
                        _state.update { it.copy(selectedGenre = intent.genre) }
                        filterProducts()
                    }
                    is ProductIntent.ClearGenre -> {
                        _state.update { it.copy(selectedGenre = null) }
                        filterProducts()
                    }
                    is ProductIntent.PreviousTopProduct -> {
                        val current = _state.value.activeTopIndex
                        val newIndex = if (current > 0) current - 1 else _state.value.topProducts.lastIndex
                        _state.update { it.copy(activeTopIndex = newIndex) }
                    }
                    is ProductIntent.NextTopProduct -> {
                        val current = _state.value.activeTopIndex
                        val newIndex = if (current < _state.value.topProducts.lastIndex) current + 1 else 0
                        _state.update { it.copy(activeTopIndex = newIndex) }
                    }
                    is ProductIntent.AddToCart -> {
                        // هنا كتزيد المنتوج للسلة. ممكن مثلا:
                        val updatedCartCount = _state.value.cartCount + 1
                        _state.update { it.copy(cartCount = updatedCartCount) }

                        // يمكن تزيد log أو message
                        Log.d("AddToCart", "Produit ajouté: ${intent.product.name}")
                    } is ProductIntent.SetSelectedProducts -> {
                    _state.update { it.copy(selectedProducts = intent.products) }
                }               }
            }
        }
    }
    fun onSearchChange(newTerm: String) {
        _state.value = _state.value.copy(searchTerm = newTerm)
    }

    fun onSearchSubmit() {
        // Implémente la logique de recherche ici
    }
    private suspend fun loadProducts() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val products = repository.getProducts()
            val genres = products.mapNotNull { it.genre }.distinct()
            _state.value = ProductViewState(
                isLoading = false,
                products = products,
                filteredProducts = products,
                topProducts = products.take(5),
                genres = genres
            )
        } catch (e: Exception) {
            _state.value = ProductViewState(isLoading = false, error = e.message ?: "Erreur de chargement")
        }
    }

    private fun filterProducts() {
        val genre = _state.value.selectedGenre
        val filtered = if (genre != null) {
            _state.value.products.filter { it.genre == genre }
        } else {
            _state.value.products
        }
        _state.update { it.copy(filteredProducts = filtered) }
    }
}
