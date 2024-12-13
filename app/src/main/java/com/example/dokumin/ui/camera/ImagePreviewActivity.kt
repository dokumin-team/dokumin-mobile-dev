package com.example.dokumin.ui.camera

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.data.repositories.DocumentRepository
import com.example.dokumin.databinding.ActivityImagePreviewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import java.io.File

class ImagePreviewActivity : AppCompatActivity() {
    private var imagePath: String? = null
    private var imageSource: String? = null
    private var imageUri: Uri? = null
    private var binding: ActivityImagePreviewBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val imageView = binding?.imageView

        imageUri = intent.getParcelableExtra("imageUri")
        imagePath = intent.getStringExtra("imagePath")
        imageSource = intent.getStringExtra("imageSource")


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

        } ?: run {
            Toast.makeText(this, "No image to display", Toast.LENGTH_SHORT).show()
        }

        binding?.btnUploadDocument?.setOnClickListener {
            imageUri?.let {

                val filePath = imageUri?.path
                // Get the filename
                val filename = filePath?.substring(filePath.lastIndexOf("/") + 1) ?: "unknown_file"

                // Use ContentResolver to get the MIME type and derive the extension
                val mimeType = contentResolver.getType(imageUri!!) ?: "application/octet-stream"
                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "unknown"

                // Append the extension to the filename if necessary
                val fullFilename = if (filename?.contains('.') == true) filename else "$filename.$extension"

                DocumentRepository.postScannedDocument(
                    uri = it,
                    context = this@ImagePreviewActivity,
                    filename = fullFilename
                )
                return@setOnClickListener
            }

            imagePath?.let {
                val uri = Uri.fromFile(File(it))
                DocumentRepository.postScannedDocument(
                    uri = uri,
                    context = this@ImagePreviewActivity,
                    filename = uri?.pathSegments?.last().toString()
                )
                return@setOnClickListener
            }
        }

        observePostScanDocumentResponse()

    }


    private fun observePostScanDocumentResponse() {
        DocumentRepository.scanDocumentResponse.observe(this, {
            if (it != null) {
                FancyToast.makeText(
                    this,
                    "Document has been uploaded",
                    FancyToast.LENGTH_LONG,
                    FancyToast.SUCCESS,
                    false
                ).show()
                DocumentRepository.clearState()
                finish()
            }
        })

        DocumentRepository.errorMessage.observe(this, {
            if (it != null) {
                FancyToast.makeText(
                    this,
                    it,
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
                DocumentRepository.clearState()
//                finish()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}