package com.example.dokumin.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dokumin.data.model.responses.document.Document
import com.example.dokumin.data.source.remote.datasource.DocumentRemoteDataSource

object DocumentRepository {

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _documentList: MutableLiveData<List<Document>> = MutableLiveData()
    val documentList: LiveData<List<Document>> = _documentList

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

}