package com.example.arbuztask

import android.app.Application
import com.example.arbuztask.data.AppContainer
import com.example.arbuztask.data.AppDataContainer

class MarketApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}