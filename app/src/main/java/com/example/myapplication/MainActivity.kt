package com.example.myapplication

import android.Manifest
import android.annotation.*
import android.app.*
import android.content.*
import android.content.pm.*
import android.media.*
import android.net.*
import android.os.*
import android.provider.*
import androidx.activity.result.contract.*
import androidx.appcompat.app.*
import androidx.core.content.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import com.example.myapplication.databinding.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), ImageAdapter.OnDeleteClickListener, ImageAdapter.OnImageClickListener {

    // Declare and initialize the RecyclerView and adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            openGallery()
        } else {
            startInstalledAppDetailsActivity(this)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the LiveData list of images from the Room database
        val imageDao = AppRoomDatabase.getInstance(this).imageDao()
        val imagesLiveData = imageDao.getAllImages()

        // Initialize the RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        imageAdapter = ImageAdapter(imagesLiveData, this, this)
        recyclerView.adapter = imageAdapter

        binding.fabButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onDeleteClick(image: ImageDataClass) {
        val imageDao = AppRoomDatabase.getInstance(this).imageDao()
        GlobalScope.launch(Dispatchers.IO) {
            imageDao.deleteImage(image)
        }
    }

    override fun onImageClick(image: ImageDataClass) {
        // Show image details separately
        val intent = Intent(this, ImageDetailsActivity::class.java)
        intent.putExtra(ImageDetailsActivity.EXTRA_IMAGE, image)
        startActivity(intent)
    }

    private fun openGallery() {
        // Launch the gallery intent
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        fun startInstalledAppDetailsActivity(context: Activity?) {
            if (context == null) {
                return
            }
            val i = Intent()
            i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:" + context.packageName)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(i)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data ?: return

            // Get the image path from the selected image URI
            val imagePath = getRealPathFromUri(selectedImageUri)

            // Create an Image instance with the necessary information
            val image = ImageDataClass(imagePath = imagePath, timestamp = System.currentTimeMillis())

            // Save the image to the database
            val imageDao = AppRoomDatabase.getInstance(this).imageDao()
            GlobalScope.launch(Dispatchers.IO) {
                imageDao.insertImage(image)
            }
        }
    }

    // Helper function to get the real path from a content URI
    private fun getRealPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            val path = it.getString(columnIndex)
            it.close()
            return path
        }
        return ""
    }
}
