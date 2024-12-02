package com.example.dokumin.ui.camera

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dokumin.R

class ImagePreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val imageView: AppCompatImageView = findViewById(R.id.imageView)

        val imageUri: Uri? = intent.getParcelableExtra("imageUri")

        imageUri?.let {
            imageView.setImageURI(it)
        } ?: run {
            Toast.makeText(this, "No image to display", Toast.LENGTH_SHORT).show()
        }
    }
}