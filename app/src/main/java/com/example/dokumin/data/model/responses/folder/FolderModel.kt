package com.example.dokumin.data.model.responses.folder

data class Folder(
    var createdAt: CreatedAt?,
    var id: String?,
    var folderName: String?
) {
    override fun toString(): String {
        return folderName ?: ""
    }
}

data class CreatedAt(
    var _nanoseconds: Int?,
    var _seconds: Int?
)