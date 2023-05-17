package com.example.myapplication

import android.os.*
import kotlinx.parcelize.*

@Parcelize
data class CommentMarker(
    val x: Float,
    val y: Float,
    val comment: String,
    var isCommentVisible: Boolean = false
) : Parcelable
