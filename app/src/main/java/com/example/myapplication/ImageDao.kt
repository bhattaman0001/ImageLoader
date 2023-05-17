package com.example.myapplication

import android.media.*
import androidx.lifecycle.*
import androidx.room.*

@Dao
interface ImageDao {
    @Insert
    suspend fun insertImage(image: ImageDataClass)

    @Query("SELECT * FROM images")
    fun getAllImages(): LiveData<List<ImageDataClass>>

    @Delete
    suspend fun deleteImage(image: ImageDataClass)
}

