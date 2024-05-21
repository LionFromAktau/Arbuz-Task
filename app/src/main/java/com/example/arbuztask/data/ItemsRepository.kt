package com.example.arbuztask.data

import androidx.room.Entity
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "Items")
interface ItemsRepository {

    fun getAllItemsByPrice(): Flow<List<Item>>

    fun getAllItemsByTitle(): Flow<List<Item>>

    fun getItem(id: Int): Flow<Item?>

    fun getSelectedItems(): Flow<List<Item>>

    fun getItemsTypeSortedByTitle(type: Type): Flow<List<Item>>

    fun getItemsTypeSortedByPrice(type: Type): Flow<List<Item>>

    suspend fun insertItem(item: Item)

    suspend fun updateItem(item: Item)

    suspend fun insertItemsFromApi()
    fun getSelectedItemsCount(): Flow<Int>

    suspend fun getItemCount(): Flow<Int>

}