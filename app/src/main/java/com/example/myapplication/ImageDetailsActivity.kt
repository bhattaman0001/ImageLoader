package com.example.myapplication

import android.app.*
import android.media.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import com.bumptech.glide.*
import java.text.*
import java.util.*

class ImageDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE = "extra_image"
    }

    private lateinit var imageView: CustomTouchImageView
    private lateinit var textViewImagePath: TextView
    private lateinit var textViewTimestamp: TextView
    private lateinit var commentMarkers: MutableList<CommentMarker>
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)

        imageView = findViewById(R.id.imageView)
        textViewImagePath = findViewById(R.id.textViewImagePath)
        textViewTimestamp = findViewById(R.id.textViewTimestamp)
        commentMarkers = mutableListOf()

        val image = intent.getParcelableExtra<ImageDataClass>(EXTRA_IMAGE)
        if (image != null) {
            loadImage(image.imagePath)
            textViewImagePath.text = image.imagePath
            val formattedTimestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                .format(Date(image.timestamp))
            textViewTimestamp.text = formattedTimestamp
        }

        // Initialize GestureDetector
        gestureDetector = GestureDetector(this, GestureListener())
        imageView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun loadImage(imagePath: String?) {
        Glide.with(this)
            .load(imagePath)
            .placeholder(R.drawable.image_drawable)
            .into(imageView)
    }

    private fun showCommentDialog(x: Float, y: Float) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_comment_marker)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.x = x.toInt()
        layoutParams.y = y.toInt()

        val editTextComment = dialog.findViewById<EditText>(R.id.comment_edit_text)
        val buttonSave = dialog.findViewById<Button>(R.id.save_button)
        val buttonCancel = dialog.findViewById<Button>(R.id.cancel_button)

        buttonSave.setOnClickListener {
            val comment = editTextComment.text.toString()
            if (comment.isNotEmpty()) {
                val marker = CommentMarker(x, y, comment, true)
                commentMarkers.add(marker)
                val customView = layoutInflater.inflate(R.layout.layout_comment, null)
                imageView.addMarker(marker, customView)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.attributes = layoutParams
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            val x = e.x - imageView.left
            val y = e.y - imageView.top
            showCommentDialog(x, y)
            return true
        }
    }
}
