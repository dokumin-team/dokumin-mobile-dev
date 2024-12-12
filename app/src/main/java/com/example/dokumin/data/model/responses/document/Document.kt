package com.example.dokumin.data.model.responses.document

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Document(
    val createdAt: CreatedAt,
    val fileName: String,
    val fileSize: Int,
    val fileType: String,
    val id: String,
    val url: String
)

data class CreatedAt(
    val _nanoseconds: Int,
    val _seconds: Int
){
    fun getCreatedAtFormatted(): String {
        val milliseconds = _seconds * 1000L
        val date = Date(milliseconds)
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }
}