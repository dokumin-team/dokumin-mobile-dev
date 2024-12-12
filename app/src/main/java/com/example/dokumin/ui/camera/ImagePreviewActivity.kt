package com.example.dokumin.ui.camera

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.databinding.ActivityImagePreviewBinding

class ImagePreviewActivity : AppCompatActivity() {
    private var binding: ActivityImagePreviewBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val imageView = binding?.imageView

        val imageUri: Uri? = intent.getParcelableExtra("imageUri")
        val imageSource = intent.getStringExtra("imageSource")

        imageUri?.let { image ->
            imageSource?.let { source ->
                if (source == "camera") {
                    imageView?.setImageURI(image)
                } else {
                    imageView?.setImageURI(image)
                }
            }
//            imageView?.setImageURI(it)
        } ?: run {
            Toast.makeText(this, "No image to display", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}