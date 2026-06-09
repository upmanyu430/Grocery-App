package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GroceryItem
import com.example.data.GroceryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class GroceryViewModel(private val repository: GroceryRepository) : ViewModel() {

    val activeItems: StateFlow<List<GroceryItem>> = repository.activeItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val historyItems: StateFlow<List<GroceryItem>> = repository.historyItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addItem(name: String, quantity: Int = 1) {
        if (name.isBlank() || quantity <= 0) return
        viewModelScope.launch {
            repository.insert(name, quantity)
        }
    }

    fun deleteItem(item: GroceryItem) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }

    fun checkoutCart() {
        viewModelScope.launch {
            repository.checkoutCart()
        }
    }

}
