package com.example.dokumin.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
        binding.ivCopyOcrResult.visibility = View.GONE
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

    private fun copyTextOcr() {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("OCR Result", resultTextOCR)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private var resultTextOCR = ""

    private fun processDetectedText(visionText: Text) {
         resultTextOCR = visionText.text
        if (resultTextOCR.isNotEmpty()) {
            binding.tvOcrResult.text = resultTextOCR
            binding.ivCopyOcrResult.visibility = View.VISIBLE
            binding.ivCopyOcrResult.setOnClickListener {
                copyTextOcr()
            }
        } else {
            binding.tvOcrResult.text = "No text detected"
        }
    }
}
