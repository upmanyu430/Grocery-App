package com.example.data

import kotlinx.coroutines.flow.Flow

class GroceryRepository(private val dao: GroceryItemDao) {
    val activeItems: Flow<List<GroceryItem>> = dao.getActiveItems()
    val historyItems: Flow<List<GroceryItem>> = dao.getHistoryItems()
    val frequentItemNames: Flow<List<String>> = dao.getFrequentItemNames()

    suspend fun getActiveItemsOnce(): List<GroceryItem> = dao.getActiveItemsOnce()
    suspend fun getHistoryItemsOnce(): List<GroceryItem> = dao.getHistoryItemsOnce()

    suspend fun insert(name: String) {
        dao.insertItem(GroceryItem(name = name))
    }

    suspend fun togglePurchased(item: GroceryItem) {
        dao.updateItem(item.copy(isPurchased = !item.isPurchased))
    }

    suspend fun delete(item: GroceryItem) {
        dao.deleteItemById(item.id)
    }

    suspend fun archivePurchased() {
        dao.archivePurchasedItems()
    }
}
