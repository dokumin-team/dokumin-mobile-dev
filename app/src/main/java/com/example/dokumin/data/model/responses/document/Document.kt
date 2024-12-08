package com.example.dokumin.data.model.responses.document

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
)