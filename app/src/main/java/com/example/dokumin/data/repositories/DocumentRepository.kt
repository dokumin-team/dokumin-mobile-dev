package com.example.dokumin.data.repositories

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dokumin.data.model.responses.document.CountDocumentResponse
import com.example.dokumin.data.model.responses.document.DocType
import com.example.dokumin.data.model.responses.document.Document
import com.example.dokumin.data.model.responses.document.ScanDocumentResponse
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
    var selectedDocType: DocType? = null

    private val _documentFolderList: MutableLiveData<List<Document>> = MutableLiveData()
    val documentFolderList: LiveData<List<Document>> = _documentFolderList

    private val _scanDocumentResponse: MutableLiveData<ScanDocumentResponse?> = MutableLiveData()
    val scanDocumentResponse: LiveData<ScanDocumentResponse?> = _scanDocumentResponse

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

    fun getDocumentsByFolder(folderId: String?) {
        DocumentRemoteDataSource.getDocumentByFolder(folderId) { it ->
            if (it.isSuccess) {
                _documentFolderList.value = it.getOrNull()?.documents
            } else {
                _errorMessage.value = it.exceptionOrNull()?.message
            }
        }
    }

    fun postScannedDocument(uri: Uri, context: Context, filename: String) {
        DocumentRemoteDataSource.postScannedDocument(
            uri,
            context = context,
            filename = filename,
        ) { result ->
            if (result.isSuccess) {
                _scanDocumentResponse.value = result.getOrNull()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun clearState() {
        _errorMessage.value = null
        _documentList.value = emptyList()
        _newestDocumentList.value = emptyList()
        _countDocument.value = null
        _documentFolderList.value = emptyList()
        _scanDocumentResponse.value = null
        selectedDocument = null

    }

}