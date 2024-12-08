package com.example.dokumin.data.model.responses.document

data class ListDocumentModel(
    val documents: List<Document>,
    val status: String
)