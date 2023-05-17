package com.example.myapplication

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.*

@Parcelize
@Entity(tableName = "images")
data class ImageDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imagePath: String,
    val timestamp: Long,
) : Parcelable


