package com.example.dokumin.helper

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import com.example.dokumin.ml.QrcodeModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageClassifierHelper(
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    fun classifyImage(bitmap: Bitmap) {
        val model = QrcodeModel.newInstance(context)

        // Proses gambar menjadi input untuk model
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(128, 128, ResizeOp.ResizeMethod.BILINEAR)) // Ukuran input model
            .build()

        // Konversi bitmap menjadi RGB
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap) // Pastikan ini memuat gambar RGB
        val processedImage = imageProcessor.process(tensorImage)
        val byteBuffer = processedImage.buffer

        // Siapkan input tensor
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Waktu inferensi
        val inferenceTime = SystemClock.uptimeMillis()
        val outputs = model.process(inputFeature0)
        val inferenceDuration = SystemClock.uptimeMillis() - inferenceTime

        // Ambil output
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // Callback ke listener
        classifierListener?.onResults(outputFeature0.floatArray.toList(), inferenceDuration)

        // Bersihkan resource model
        model.close()
    }


    interface ClassifierListener {
        fun onResults(results: List<Float>, inferenceTime: Long)
    }
}
