package com.example.dokumin.data.file

data class FileItem(
    val fileName: String,
    val imageResId: Int,
    val date: String = "12 Dec 2024", // Example date
    val time: String = "10:30 AM" // Example time
)