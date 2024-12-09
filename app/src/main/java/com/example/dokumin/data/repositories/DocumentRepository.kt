package com.example.dokumin.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dokumin.data.model.responses.document.CountDocumentResponse
import com.example.dokumin.data.model.responses.document.DocType
import com.example.dokumin.data.model.responses.document.Document
import com.example.dokumin.data.source.remote.datasource.DocumentRemoteDataSource

object DocumentRepository {

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _documentList: MutableLiveData<List<Document>> = MutableLiveData()
    val documentList: LiveData<List<Document>> = _documentList

    private val _newestDocumentList: MutableLiveData<List<Document>> = MutableLiveData()
    val newestDocumentList: LiveData<List<Document>> = _newestDocumentList

    private val _countDocument: MutableLiveData<CountDocumentResponse?> = MutableLiveData()
    val countDocument: LiveData<CountDocumentResponse?> = _countDocument

    var selectedDocument: Document? = null
    var selectedDocType : DocType? = null

    fun getDocuments() {
        DocumentRemoteDataSource.getDocument(
            onResult = { result ->
                if (result.isSuccess) {
                    _documentList.value = result.getOrNull()?.documents
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            }
        )
    }

    fun getNewestDocuments() {
        DocumentRemoteDataSource.getNewestDocument(
            onResult = { result ->
                if (result.isSuccess) {
                    _newestDocumentList.value = result.getOrNull()?.documents
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            }
        )
    }

    fun getCountDocuments() {
        DocumentRemoteDataSource.getCountDocument(
            onResult = { result ->
                if (result.isSuccess) {
                    _countDocument.value = result.getOrNull()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            }
        )
    }

}