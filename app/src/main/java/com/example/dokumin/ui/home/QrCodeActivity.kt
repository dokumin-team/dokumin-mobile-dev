package com.example.dokumin.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.R
import com.example.dokumin.databinding.ActivityQrCodeBinding
import com.example.dokumin.helper.ImageClassifierHelper
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.shashank.sony.fancytoastlib.FancyToast

class QrCodeActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {

    private lateinit var binding: ActivityQrCodeBinding
    private lateinit var classifierHelper: ImageClassifierHelper
    private lateinit var barcodeScanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classifierHelper = ImageClassifierHelper(this, this)
        barcodeScanner = BarcodeScanning.getClient()

        val imageUri = intent.extras?.getParcelable<Uri>("imageUri")
        imageUri?.let { uri ->
            displayImageFromUri(uri)
        }

        binding.btnAnalisisQr.setOnClickListener {
            imageUri?.let { uri ->
                processAndClassifyImage(uri)
            }
        }

        binding.ivCopyIcon.setOnClickListener {
            copyTextToClipboard(binding.tvQrCodeResult.text.toString())
        }
    }

    private fun copyTextToClipboard(text: String) {
        if (text.isNotEmpty()) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("QR Code Result", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Teks berhasil disalin ke clipboard", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Tidak ada teks untuk disalin", Toast.LENGTH_SHORT).show()
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

        val highestResult = results.maxOrNull() ?: 0f
        if (highestResult >= 0.7f) {
            binding.ivQr.drawable?.let { drawable ->
                val bitmap = (drawable as BitmapDrawable).bitmap
                scanQRCode(bitmap)
            }
        } else {
            FancyToast.makeText(
                this,
                "This image is not a barcode",
                FancyToast.LENGTH_LONG,
                FancyToast.SUCCESS,
                false
            ).show()
        }
    }

    private fun scanQRCode(bitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    processBarcode(barcode)
                }
            }
            .addOnFailureListener { e ->
                FancyToast.makeText(
                    this,
                    "Failed to scan QR code",
                    FancyToast.LENGTH_LONG,
                    FancyToast.SUCCESS,
                    false
                ).show()
            }
    }

    private fun processBarcode(barcode: Barcode) {
        val value = barcode.displayValue
        if (value != null) {
            binding.tvQrCodeResult.text = "QR Code Data: $value"
        }
    }
}
