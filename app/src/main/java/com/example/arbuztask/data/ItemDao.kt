package com.example.arbuztask.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Item>)

    @Update
    suspend fun update(item: Item)

    @Query("SELECT * FROM Items WHERE type = :type ORDER BY price ASC")
    fun getItemsTypeSortedByPrice(type: Type): Flow<List<Item>>

    @Query("SELECT * FROM Items WHERE type = :type ORDER BY title ASC")
    fun getItemsTypeSortedByTitle(type: Type): Flow<List<Item>>

    @Query("SELECT * FROM Items WHERE selected = 1")
    fun getSelectedItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * FROM items ORDER BY price")
    fun getAllItemsByPrice(): Flow<List<Item>>

    @Query("SELECT * FROM items ORDER BY title")
    fun getAllItemsByTitle(): Flow<List<Item>>

    @Query("SELECT COUNT(*) FROM items WHERE selected = 1")
    fun getSelectedItemsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM items")
    fun getItemCount(): Flow<Int>


}