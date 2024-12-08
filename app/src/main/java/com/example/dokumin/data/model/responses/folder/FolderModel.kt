package com.example.dokumin.data.model.responses.folder

data class Folder(
    var createdAt: CreatedAt?,
    var id: String?,
    var name: String?
)

data class CreatedAt(
    var _nanoseconds: Int?,
    var _seconds: Int?
)