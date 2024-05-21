package com.example.arbuztask

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arbuztask.data.Item
import com.example.arbuztask.data.ItemsRepository
import com.example.arbuztask.data.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember

class AppViewModel(private val itemsRepository: ItemsRepository): ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


    lateinit var uiState: StateFlow<List<Item>>
        private set

    val selectedItems: StateFlow<List<Item>?> = itemsRepository.getSelectedItems()
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = null
    )



    val selectedItemsQuantity: StateFlow<Int?> = itemsRepository.getSelectedItemsCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = null
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            uiState = itemsRepository.getAllItemsByPrice()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = itemsRepository.getAllItemsByPrice().first()
                )
            if(uiState.value.size == 0){
                insertItemsFromApi()
            }
        }
    }



    fun setItemQuantity(item: Item, isAdded: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            val quantity = item.quantity + if (isAdded) 1 else -1
            itemsRepository.updateItem(item.copy(
                quantity = quantity,
                selected = quantity > 0
            ))
        }
    }

    fun updateItems(isSortedByPrice: Boolean, type: Type){
        viewModelScope.launch(Dispatchers.IO) {
            val flow: Flow<List<Item>> = when(type){
                Type.All -> {
                    if(isSortedByPrice){
                        itemsRepository.getAllItemsByPrice()
                    }else{
                        itemsRepository.getAllItemsByTitle()
                    }
                }

                else -> {
                    if(isSortedByPrice){
                        itemsRepository.getItemsTypeSortedByPrice(type)
                    }else{
                        itemsRepository.getItemsTypeSortedByTitle(type)
                    }
                }
            }
            uiState = flow.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = flow.first()
            )
        }

    }

    private fun insertItemsFromApi(){
         viewModelScope.launch {
             itemsRepository.insertItemsFromApi()
         }
    }


}


