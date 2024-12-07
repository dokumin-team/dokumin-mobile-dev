package com.example.dokumin.ui.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.dokumin.R
import java.io.File
import java.util.concurrent.Executors

class CameraXActivity : AppCompatActivity() {

    private lateinit var capture: ImageButton
    private lateinit var toggleFlash: ImageButton
    private lateinit var flipCamera: ImageButton
    private lateinit var previewView: PreviewView

    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private var cameraFacing: Int = CameraSelector.LENS_FACING_BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_x)

        previewView = findViewById(R.id.cameraPreview)
        capture = findViewById(R.id.capture)
        toggleFlash = findViewById(R.id.toggleFlash)
        flipCamera = findViewById(R.id.flipCamera)

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startCamera(cameraFacing)
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startCamera(cameraFacing)
        }

        flipCamera.setOnClickListener {
            cameraFacing = if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            startCamera(cameraFacing)
        }
    }

    private fun startCamera(cameraFacing: Int) {
        val aspectRatio = aspectRatio(previewView.width, previewView.height)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .setTargetAspectRatio(aspectRatio)
                .build()

            val imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(windowManager.defaultDisplay.rotation)
                .build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(cameraFacing)
                .build()

            cameraProvider.unbindAll()

            val camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )

            capture.setOnClickListener {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } else {
                    takePicture(imageCapture)
                }
            }

            toggleFlash.setOnClickListener {
                setFlashIcon(camera)
            }

            preview.setSurfaceProvider(previewView.surfaceProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePicture(imageCapture: ImageCapture) {
        val file = File(getExternalFilesDir(null), "${System.currentTimeMillis()}.jpg")
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                runOnUiThread {
                    Toast.makeText(this@CameraXActivity, "Image saved at: ${file.path}", Toast.LENGTH_SHORT).show()
                }
                startCamera(cameraFacing)
            }

            override fun onError(exception: ImageCaptureException) {
                runOnUiThread {
                    Toast.makeText(this@CameraXActivity, "Failed to save: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
                startCamera(cameraFacing)
            }
        })
    }

    private fun setFlashIcon(camera: Camera) {
        if (camera.cameraInfo.hasFlashUnit()) {
            val torchState = camera.cameraInfo.torchState.value
            if (torchState == TorchState.OFF) {
                camera.cameraControl.enableTorch(true)
                toggleFlash.setImageResource(R.drawable.ic_flash_off)
            } else {
                camera.cameraControl.enableTorch(false)
                toggleFlash.setImageResource(R.drawable.ic_flash_on)
            }
        } else {
            runOnUiThread {
                Toast.makeText(this, "Flash is not available currently", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = maxOf(width, height).toDouble() / minOf(width, height)
        return if (kotlin.math.abs(previewRatio - 4.0 / 3.0) <= kotlin.math.abs(previewRatio - 16.0 / 9.0)) {
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }
}
