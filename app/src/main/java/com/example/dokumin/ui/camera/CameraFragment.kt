    package com.example.dokumin.ui.camera

    import android.Manifest
    import android.app.Activity
    import android.content.Intent
    import android.net.Uri
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.core.app.ActivityCompat
    import androidx.core.content.ContextCompat
    import androidx.fragment.app.Fragment
    import com.example.dokumin.R
    import android.provider.MediaStore
    import android.content.pm.PackageManager
    import android.widget.ImageView

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
            val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(requireActivity(), permissions, 1)
            }
        }

        private fun openCamera() {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }

        private fun openGallery() {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = data?.data

                if (requestCode == CAMERA_REQUEST_CODE || requestCode == GALLERY_REQUEST_CODE) {
                    imageUri?.let {
                        val intent = Intent(requireContext(), ImagePreviewActivity::class.java)
                        intent.putExtra("imageUri", it)
                        startActivity(intent)
                    }
                }
            }
        }
    }
