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

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(128, 128, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        val processedImage = imageProcessor.process(tensorImage)
        val byteBuffer = processedImage.buffer

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val inferenceTime = SystemClock.uptimeMillis()
        val outputs = model.process(inputFeature0)
        val inferenceDuration = SystemClock.uptimeMillis() - inferenceTime

        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        classifierListener?.onResults(outputFeature0.floatArray.toList(), inferenceDuration)

        model.close()
    }


    interface ClassifierListener {
        fun onResults(results: List<Float>, inferenceTime: Long)
    }
}
