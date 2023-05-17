package com.example.myapplication

import androidx.room.*

@Dao
interface ImageDao {
    @Insert
    suspend fun insertImage(image: ImageDataClass)

    @Query("SELECT * FROM images")
    suspend fun getAllImages(): List<ImageDataClass>
}

