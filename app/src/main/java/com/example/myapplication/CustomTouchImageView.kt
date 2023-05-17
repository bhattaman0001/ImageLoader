package com.example.myapplication

import android.annotation.*
import android.content.*
import android.graphics.*
import android.graphics.drawable.*
import android.util.*
import android.view.*
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.widget.*
import androidx.core.content.*
import com.google.android.material.snackbar.*

class CustomTouchImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val commentMarkers: MutableList<CommentMarker> = mutableListOf()
    private var markerDrawable: Drawable? = null
    private var customCommentLayout: View? = null

    init {
        // Load the marker drawable once
        markerDrawable = ContextCompat.getDrawable(context, R.drawable.logo)
    }

    fun addMarker(marker: CommentMarker, customCommentLayout: View) {
        commentMarkers.add(marker)
        this.customCommentLayout = customCommentLayout
        invalidate() // Trigger a redraw to display the marker
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw the comment markers on the canvas
        markerDrawable?.let { drawable ->
            for (marker in commentMarkers) {
                val markerRect = Rect(
                    (marker.x - drawable.intrinsicWidth / 2).toInt(),
                    (marker.y - drawable.intrinsicHeight / 2).toInt(),
                    (marker.x + drawable.intrinsicWidth / 2).toInt(),
                    (marker.y + drawable.intrinsicHeight / 2).toInt()
                )
                drawable.bounds = markerRect
                drawable.draw(canvas)
                Toast.makeText(context.applicationContext, "Your Comment is : ${marker.comment}", Toast.LENGTH_LONG).show()
                val cTV: TextView? = customCommentLayout?.findViewById<TextView>(R.id.textViewComment)
                cTV?.text = marker.comment
                Log.d("comment_layout", "comment is: ${cTV?.text}")
                /*if (marker.isCommentVisible) {
                    val markerPaint = Paint().apply {
                        color = Color.RED
                        style = Paint.Style.FILL
                        textSize = 16f
                    }
                    canvas.drawText(
                        marker.comment,
                        marker.x,
                        marker.y + drawable.intrinsicHeight,
                        markerPaint
                    )
                }*/
            }
        }
    }
}
