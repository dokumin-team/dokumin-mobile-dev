package com.example.dokumin.ui.home

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.databinding.ActivityOcrBinding

class OcrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOcrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOcrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.extras?.getParcelable<Uri>("imageUri")
        imageUri?.let { uri ->
            displayImageFromUri(uri)
        }
    }

    private fun displayImageFromUri(uri: Uri) {
        try {
            val imageStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            binding.ivOcrPreview.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
