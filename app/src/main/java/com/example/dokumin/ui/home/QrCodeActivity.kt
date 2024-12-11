package com.example.dokumin.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.R
import com.example.dokumin.databinding.ActivityQrCodeBinding
import com.example.dokumin.helper.ImageClassifierHelper

class QrCodeActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {

    private lateinit var binding: ActivityQrCodeBinding
    private lateinit var classifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classifierHelper = ImageClassifierHelper(this, this)

        val imageUri = intent.extras?.getParcelable<Uri>("imageUri")
        imageUri?.let { uri ->
            displayImageFromUri(uri)
        }

        binding.btnAnalisisQr.setOnClickListener {
            imageUri?.let { uri ->
                processAndClassifyImage(uri)
            }
        }
    }


    private fun displayImageFromUri(uri: Uri) {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.ivQr.setImageBitmap(bitmap)
    }

    private fun processAndClassifyImage(uri: Uri) {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        bitmap?.let { classifyImage(it) }
    }


    private fun classifyImage(bitmap: Bitmap) {
        classifierHelper.classifyImage(bitmap)
    }


    override fun onResults(results: List<Float>, inferenceTime: Long) {
        val resultText = results.joinToString(prefix = "Hasil: ", separator = ", ") { "${it * 100}%" }
        binding.tvResult.text = "$resultText\nWaktu inferensi: $inferenceTime ms"
    }
}