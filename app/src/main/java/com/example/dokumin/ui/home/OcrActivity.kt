package com.example.dokumin.ui.home

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.databinding.ActivityOcrBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class OcrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOcrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOcrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.extras?.getParcelable<Uri>("imageUri")
        imageUri?.let { uri ->
            displayImageFromUri(uri)
            performOcr(uri)
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

    private fun performOcr(uri: Uri) {
        try {
            val imageStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            val inputImage = InputImage.fromBitmap(bitmap, 0)

            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    processDetectedText(visionText)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Log.e("OCR", "Text recognition failed", e)
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun processDetectedText(visionText: Text) {
        val resultText = visionText.text
        if (resultText.isNotEmpty()) {
            binding.tvOcrResult.text = resultText
        } else {
            binding.tvOcrResult.text = "No text detected"
        }
    }
}
