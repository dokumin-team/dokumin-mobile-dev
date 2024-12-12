package com.example.dokumin.data.model.responses.document

data class ScanDocumentResponse (
    var success : String,
    var classification : String,
    var documentId : String,
    var url: String,
    var folderName : String,
    var confidence : Double,
)