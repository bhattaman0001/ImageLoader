package com.example.myapplication

import android.view.*
import android.widget.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import java.text.*
import java.util.*

class ImageAdapter(private val imagesLiveData: LiveData<List<ImageDataClass>>, private val onDeleteClickListener: OnDeleteClickListener) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var images: List<ImageDataClass> = emptyList()

    init {
        imagesLiveData.observeForever { updatedImages ->
            images = updatedImages
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(image: ImageDataClass)
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textViewImagePath: TextView = itemView.findViewById(R.id.textViewImagePath)
        private val textViewTimestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(image: ImageDataClass) {
            // Load and display the image using a library like Glide or Picasso
            Glide.with(itemView.context)
                .load(image.imagePath)
                .placeholder(R.drawable.image_drawable)
                .into(imageView)

            textViewImagePath.text = image.imagePath

            val formattedTimestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                .format(Date(image.timestamp))
            textViewTimestamp.text = formattedTimestamp

            deleteButton.setOnClickListener {
                onDeleteClickListener.onDeleteClick(image)
            }
        }
    }
}
