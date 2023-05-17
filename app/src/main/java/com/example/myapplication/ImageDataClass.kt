package com.example.myapplication

import androidx.room.*

@Entity(tableName = "images")
data class ImageDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imagePath: String,
    val timestamp: Long,
)


