package com.example.dokumin.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dokumin.data.model.responses.folder.ListFolderModel
import com.example.dokumin.data.source.remote.datasource.FolderRemoteDataSource

object FolderReposiotry {

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage : LiveData<String?> = _errorMessage

    private val _folderList: MutableLiveData<ListFolderModel?> = MutableLiveData()
    val folderList : LiveData<ListFolderModel?> = _folderList


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


}