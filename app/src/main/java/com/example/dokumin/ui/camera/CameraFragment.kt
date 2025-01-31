package com.example.dokumin.ui.camera

import android.Manifest
import android.animation.ObjectAnimator
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import com.example.dokumin.databinding.FragmentCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private val GALLERY_REQUEST_CODE = 200
    private lateinit var currentPath: String
    private var getFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnimation()
        checkPermissions()

        binding.cameraBtn.setOnClickListener {
            openCamera()
        }

        binding.galleryBtn.setOnClickListener {
            openGalleryMenu()
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

    private fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private val timeStamp: String = SimpleDateFormat(
        "dd-MMM-yyyy",
        Locale.getDefault()
    ).format(System.currentTimeMillis())

    fun openCamera() {
        if (isAdded && context != null) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            createTempFile(requireContext()).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireActivity(),
                    "com.example.dokumin",
                    it
                )
                currentPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraIntentLauncher.launch(intent)
            }
        } else {
            Log.e("CameraFragment", "Fragment is not attached to a context.")
        }
    }

    fun openGalleryMenu() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
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

    private val cameraIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPath)
            getFile = myFile

            val result = getFile?.path
            val intent = Intent(requireContext(), ImagePreviewActivity::class.java)
            intent.putExtra("imagePath", result)
            intent.putExtra("imageSource", "camera")
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView5, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}
