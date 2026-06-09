package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "grocery_items")
data class GroceryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val isPurchased: Boolean = false,
    val isArchived: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface GroceryItemDao {
    @Query("SELECT * FROM grocery_items WHERE isArchived = 0 ORDER BY isPurchased ASC, timestamp DESC")
    fun getActiveItems(): Flow<List<GroceryItem>>

    @Query("SELECT * FROM grocery_items WHERE isArchived = 0")
    suspend fun getActiveItemsOnce(): List<GroceryItem>

    @Query("SELECT * FROM grocery_items WHERE isArchived = 1 ORDER BY timestamp DESC")
    fun getHistoryItems(): Flow<List<GroceryItem>>

    @Query("SELECT * FROM grocery_items WHERE isArchived = 1 ORDER BY timestamp DESC")
    suspend fun getHistoryItemsOnce(): List<GroceryItem>

    @Query("SELECT name FROM grocery_items GROUP BY name ORDER BY COUNT(id) DESC LIMIT 50")
    fun getFrequentItemNames(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: GroceryItem)

    @Update
    suspend fun updateItem(item: GroceryItem)

    @Query("DELETE FROM grocery_items WHERE id = :id")
    suspend fun deleteItemById(id: Int)

    @Query("UPDATE grocery_items SET isArchived = 1, isPurchased = 1 WHERE isArchived = 0")
    suspend fun checkoutAllActiveItems()
}

@Database(entities = [GroceryItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groceryItemDao(): GroceryItemDao
}
