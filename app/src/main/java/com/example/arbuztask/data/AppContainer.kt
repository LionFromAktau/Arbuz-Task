package com.example.arbuztask.data

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

interface AppContainer {
    val itemsRepository: ItemsRepository
}

class AppDataContainer(private val context: Context): AppContainer{

    override val itemsRepository: ItemsRepository by lazy {
        DataItemsRepository(MarketDataBase.getDataBase(context).itemDao())
    }
}



