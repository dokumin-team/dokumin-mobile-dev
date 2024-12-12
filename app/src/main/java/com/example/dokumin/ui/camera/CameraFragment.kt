package com.example.dokumin.ui.camera

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.dokumin.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CameraFragment : Fragment() {

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraBtn = view.findViewById<ImageView>(R.id.cameraBtn)
        val galleryBtn = view.findViewById<ImageView>(R.id.galleryBtn)


        checkPermissions()

        cameraBtn.setOnClickListener {
            openCamera()
        }

        galleryBtn.setOnClickListener {
            openGallery()
        }
    }

    private fun checkPermissions() {
        val permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(requireActivity(), permissions, 1)
        }
    }

    fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private val timeStamp: String = SimpleDateFormat(
        "dd-MMM-yyyy",
        Locale.getDefault()
    ).format(System.currentTimeMillis())


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.resolveActivity(packageManager)

        createTempFile(requireContext()).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireActivity(),
                "com.asthiseta.submissionintermediate",
                it
            )
            currentPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraIntentLauncher.launch(intent)
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data

            if (requestCode == GALLERY_REQUEST_CODE) {
                imageUri?.let {
                    val intent = Intent(requireContext(), ImagePreviewActivity::class.java)
                    intent.putExtra("imageUri", it)
                    intent.putExtra("imageSource", "gallery")
                    startActivity(intent)
                }
            }

        }
    }


    private lateinit var currentPath: String
    private var getFile: File? = null

    private val cameraIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPath)
            getFile = myFile

            // TODO MOve To ImagePreviewActivity
//            val result = BitmapFactory.decodeFile(getFile?.path)
//            binding.imageViewPreview.setImageBitmap(result)
        }
    }

//        companion object {
//            private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
//            private const val REQUEST_CODE_PERMISSIONS = 10
//            const val MY_LOCATION_TO_SHARE = 11
//        }
}
