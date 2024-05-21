package com.example.arbuztask.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class MarketDataBase: RoomDatabase() {
    abstract fun itemDao(): ItemDao
    companion object{
        @Volatile
        private var Instance: MarketDataBase? = null

        fun getDataBase(context: Context): MarketDataBase{
            return Instance ?: synchronized(this){ Room.databaseBuilder(
                context,
                MarketDataBase::class.java,
                "item_database")
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
            }
        }
    }
}