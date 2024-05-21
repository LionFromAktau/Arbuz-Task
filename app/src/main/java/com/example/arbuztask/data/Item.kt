package com.example.arbuztask.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: Type,
    val price: Int,
    val quantity: Int = 0,
    val selected: Boolean = false,
    val title: String,
    val image: String,
)

fun Item.getTotalPrice(): Int{
    if(quantity != 0){
        return price * quantity
    }
    return price
}