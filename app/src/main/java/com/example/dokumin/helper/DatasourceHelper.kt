package com.example.dokumin.helper

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DatasourceHelper {

    fun prepareFilePart(
        partname: String,
        filename: String,
        fileUri: Uri,
        context: Context
    ): MultipartBody.Part? {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(fileUri) ?: "application/octet-stream"

        val inputStream = contentResolver.openInputStream(fileUri)
        val requestBody = inputStream?.readBytes()?.toRequestBody(mimeType.toMediaTypeOrNull())
            ?: throw IllegalArgumentException("Unable to open InputStream for the provided Uri")

        return MultipartBody.Part.createFormData(partname, filename, requestBody)
    }
}