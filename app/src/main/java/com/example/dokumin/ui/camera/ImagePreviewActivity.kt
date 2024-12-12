package com.example.dokumin.ui.camera

import android.graphics.BitmapFactory
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
        val imagePath: String? = intent.getStringExtra("imagePath")
        val imageSource = intent.getStringExtra("imageSource")


        imageSource?.let { source ->
            if (source == "camera") {
                imagePath?.let { image ->
                    val result = BitmapFactory.decodeFile(image)
                    imageView?.setImageBitmap(result)

                } ?: run {
                    Toast.makeText(this, "No image to display", Toast.LENGTH_SHORT).show()
                }

            } else {
                imageUri?.let { image ->
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