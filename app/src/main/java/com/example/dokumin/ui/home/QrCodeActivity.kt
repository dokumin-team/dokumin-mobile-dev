package com.example.dokumin.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.R
import com.example.dokumin.helper.ImageClassifierHelper

class QrCodeActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {

    private lateinit var classifierHelper: ImageClassifierHelper
    private lateinit var resultTextView: TextView
    private lateinit var qrImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)

        qrImageView = findViewById(R.id.iv_qr)
        resultTextView = findViewById(R.id.tvResult)
        val analyzeButton = findViewById<Button>(R.id.btnAnalisisQr)

        val imageUri = intent.extras?.getParcelable<Uri>("imageUri")
        imageUri?.let {
            val imageStream = contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            qrImageView.setImageBitmap(bitmap)
        }

        classifierHelper = ImageClassifierHelper(this, this)

        analyzeButton.setOnClickListener {
            imageUri?.let { uri ->
                val imageStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(imageStream)
                bitmap?.let { bmp ->
                    classifyImage(bmp)
                }
            }
        }
    }

    private fun classifyImage(bitmap: Bitmap) {
        classifierHelper.classifyImage(bitmap)
    }

    override fun onResults(results: List<Float>, inferenceTime: Long) {
        val resultText = results.joinToString(prefix = "Hasil: ", separator = ", ")
        resultTextView.text = "$resultText\nWaktu inferensi: $inferenceTime ms"
    }
}
