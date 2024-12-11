package com.example.dokumin.data.repositories

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dokumin.data.model.responses.folder.CountFolderResponse
import com.example.dokumin.data.model.responses.folder.CreateFolderModel
import com.example.dokumin.data.model.responses.folder.Folder
import com.example.dokumin.data.model.responses.folder.ListFolderModel
import com.example.dokumin.data.model.responses.folder.UploadDocumentModel
import com.example.dokumin.data.source.remote.datasource.FolderRemoteDataSource

object FolderRepository {

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage : LiveData<String?> = _errorMessage

    private val _folderList: MutableLiveData<ListFolderModel?> = MutableLiveData()
    val folderList : LiveData<ListFolderModel?> = _folderList

    private val _countFolder: MutableLiveData<CountFolderResponse?> = MutableLiveData()
    val countFolder: LiveData<CountFolderResponse?> = _countFolder

    var selectedFolder: Folder? = null
    private val _postDocumentToFolderResponse: MutableLiveData<UploadDocumentModel?> = MutableLiveData()
    val postDocumentToFolderResponse: LiveData<UploadDocumentModel?> = _postDocumentToFolderResponse

    private val _createFolderResponse: MutableLiveData<CreateFolderModel?> = MutableLiveData()
    val createFolderResponse: LiveData<CreateFolderModel?> = _createFolderResponse

    fun getFolders(){
        FolderRemoteDataSource.getFolders(
            onResult = { result ->
                if (result.isSuccess){
                    _folderList.value = result.getOrNull()
                }else{
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            }
        )
    }

    fun getCountFolder() {
        FolderRemoteDataSource.getCountFolder(
            onResult = { result ->
                if (result.isSuccess) {
                    _countFolder.value = result.getOrNull()
                } else {
                    FolderRepository._errorMessage.value = result.exceptionOrNull()?.message
                }
            }
        )
    }

    fun postDocumentToFolder(uri: Uri, context: Context, filename: String){
        FolderRemoteDataSource.postDocumentToFolder(
            folderId = selectedFolder?.id.toString(),
            context = context,
            fileUri = uri,
            filename = filename,
            onResult = { result ->
                if (result.isSuccess){
                    _postDocumentToFolderResponse.value = result.getOrNull()
                }else{
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            }

        )
    }

    fun postFolder(folderName: String) {
        FolderRemoteDataSource.postFolder(folderName) { result ->
            if (result.isSuccess) {
                _createFolderResponse.value = result.getOrNull()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }



}