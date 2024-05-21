package com.example.arbuztask.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.arbuztask.AppViewModel
import com.example.arbuztask.MarketApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AppViewModel(marketApplication().container.itemsRepository)
        }
    }
}

fun CreationExtras.marketApplication(): MarketApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MarketApplication)