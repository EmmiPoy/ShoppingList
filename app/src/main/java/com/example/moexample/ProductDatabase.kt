package com.example.moexample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[Product::class, Kategory::class], version = 1, exportSchema=false)
abstract class ProductDatabase : RoomDatabase() {
    abstract val productDatabaseDao : ProductDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance==null) {
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java,
                        "product_db.db"
                    )
                        .fallbackToDestructiveMigration().build()
                    INSTANCE=instance
                }

                return instance
            }
        }
    }
}

