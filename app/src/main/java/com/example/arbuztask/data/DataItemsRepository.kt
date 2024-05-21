package com.example.arbuztask.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class DataItemsRepository(private val itemDao: ItemDao): ItemsRepository {

    private val client = OkHttpClient()
    private val api_key = "XpdwSX5VzEk6h-z5bXPrIvs1NSqzNqXxlUHgMb966pY"
    private val url = "https://api.unsplash.com/search/photos?page=1&query="
    private val randomGenerator = RandomGenerator()


    override suspend fun insertItemsFromApi(){
        val foodQuery = "daily-food-products"
        val drinkQuery = "drinks"

        val requestDrink = Request.Builder()
            .url("$url$drinkQuery")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept-Version", "v1")
            .addHeader("Authorization", "Client-ID $api_key")
            .get()
            .build()

        val requestFood = Request.Builder()
            .url("$url$foodQuery")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept-Version", "v1")
            .addHeader("Authorization", "Client-ID $api_key")
            .get()
            .build()

        withContext(Dispatchers.IO){
            try {
                val responseDrink = client.newCall(requestDrink).execute()
                if (responseDrink.isSuccessful) {
                    responseDrink.body?.let {
                        val body = it.string()
                        val data = JSONObject(body).getJSONArray("results")
                        val items = mutableListOf<Item>()
                        for (i in 0 until data.length()) {
                            val image = data.getJSONObject(i).getJSONObject("urls").getString("small").replace("\\", "")
                            items.add(Item(
                                type = Type.Drink,
                                price = randomGenerator.getRandomPrice(),
                                title = randomGenerator.getRandomDrinkName(),
                                image = image
                            ))
                        }
                        itemDao.insertItems(items)
                    }
                } else {
                    throw IOException(">>> Unexpected code $responseDrink")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(">>> Error in Drink Request", e.message.toString())
            }


            try {
                val responseFood = client.newCall(requestFood).execute()
                if (responseFood.isSuccessful) {
                    responseFood.body?.let {
                        val body = it.string()
                        val data = JSONObject(body).getJSONArray("results")
                        val items = mutableListOf<Item>()
                        for (i in 0 until data.length()) {
                            val image = data.getJSONObject(i).getJSONObject("urls").getString("regular").replace("\\", "")
                            items.add(Item(
                                type = Type.Food,
                                price = randomGenerator.getRandomPrice(),
                                title = randomGenerator.getRandomFoodName(),
                                image = image
                            ))
                        }
                        itemDao.insertItems(items)
                    }
                } else {
                    throw IOException(">>> Unexpected code $responseFood")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(">>> Error in Food Request", e.message.toString())
            }



        }

    }

    override fun getSelectedItemsCount(): Flow<Int> {
        return itemDao.getSelectedItemsCount()
    }


    override fun getAllItemsByPrice(): Flow<List<Item>>{
        return itemDao.getAllItemsByPrice()
    }

    override fun getAllItemsByTitle(): Flow<List<Item>> {
        return itemDao.getAllItemsByTitle()
    }

    override fun getItem(id: Int): Flow<Item?> {
        return itemDao.getItem(id)
    }

    override fun getSelectedItems(): Flow<List<Item>> {
        return itemDao.getSelectedItems()
    }

    override fun getItemsTypeSortedByPrice(type: Type): Flow<List<Item>> {
        return itemDao.getItemsTypeSortedByPrice(type)
    }

    override fun getItemsTypeSortedByTitle(type: Type): Flow<List<Item>> {
        return itemDao.getItemsTypeSortedByTitle(type)
    }

    override suspend fun getItemCount(): Flow<Int> {
        return itemDao.getItemCount()
    }

    override suspend fun insertItem(item: Item) {
        itemDao.insert(item)
    }

    override suspend fun updateItem(item: Item) {
        itemDao.update(item)
    }

}

