package com.example.myapplication

import android.content.*
import androidx.room.*

@Database(entities = [ImageDataClass::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao

    companion object {
        @Volatile
        private var instance: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "image_database"
                ).build()
                instance = database
                database
            }
        }
    }
}
