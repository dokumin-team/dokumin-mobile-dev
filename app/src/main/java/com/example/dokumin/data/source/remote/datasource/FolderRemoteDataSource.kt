package com.example.dokumin.data.source.remote.datasource

import android.content.Context
import android.net.Uri
import com.example.dokumin.data.model.responses.folder.CountFolderResponse
import com.example.dokumin.data.model.responses.folder.CreateFolderModel
import com.example.dokumin.data.model.responses.folder.ListFolderModel
import com.example.dokumin.data.model.responses.folder.UploadDocumentModel
import com.example.dokumin.data.source.remote.RetrofitConfig
import com.example.dokumin.helper.DatasourceHelper.prepareFilePart
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FolderRemoteDataSource {

    fun getFolders(
        onResult: (Result<ListFolderModel?>) -> Unit,
    ) {
        val call = RetrofitConfig.ApiService.getFolders(
            "Bearer ${RetrofitConfig.token}"
        )
        call.enqueue(
            object : Callback<ListFolderModel?> {
                override fun onResponse(
                    call: Call<ListFolderModel?>,
                    response: Response<ListFolderModel?>
                ) {
                    if (response.isSuccessful) {
                        onResult(Result.success(response.body()))
                    } else {
                        val errorJsonString = response.errorBody()?.string()
                        val message = try {
                            val jsonObject = JSONObject(errorJsonString.toString())
                            jsonObject.optString("message", "Unknown error")
                        } catch (e: Exception) {
                            "Error parsing response: ${e.message}"
                        }
                        onResult(Result.failure(Throwable(message)))
                    }
                }

                override fun onFailure(call: Call<ListFolderModel?>, throwable: Throwable) {
                    onResult(Result.failure(throwable))
                }

            }
        )

    }

    fun getCountFolder(
        onResult: (Result<CountFolderResponse?>) -> Unit,
    ) {
        val call = RetrofitConfig.ApiService.getCountFolder(
            "Bearer ${RetrofitConfig.token}"
        )
        call.enqueue(
            object : Callback<CountFolderResponse?> {
                override fun onResponse(
                    call: Call<CountFolderResponse?>,
                    response: Response<CountFolderResponse?>
                ) {
                    if (response.isSuccessful) {
                        onResult(Result.success(response.body()))
                    } else {
                        val errorJsonString = response.errorBody()?.string()
                        val message = try {
                            val jsonObject = JSONObject(errorJsonString.toString())
                            jsonObject.optString("message", "Unknown error")
                        } catch (e: Exception) {
                            "Error parsing response: ${e.message}"
                        }
                        onResult(Result.failure(Throwable(message)))

                    }
                }

                override fun onFailure(call: Call<CountFolderResponse?>, t: Throwable) {
                    onResult(Result.failure(t))
                }
            }
        )
    }

    fun postDocumentToFolder(
        folderId: String,
        filename: String,
        fileUri: Uri,
        context: Context,
        onResult: (Result<UploadDocumentModel?>) -> Unit
    ) {
        val filePart = prepareFilePart("file", filename, fileUri, context)

        val call = RetrofitConfig.ApiService.postDocumentToFolder(
            "Bearer ${RetrofitConfig.token}",
            folderId,
            filename = filename,
            file = filePart
        )
        call.enqueue(
            object : Callback<UploadDocumentModel?> {
                override fun onResponse(
                    call: Call<UploadDocumentModel?>,
                    response: Response<UploadDocumentModel?>
                ) {
                    if (response.isSuccessful) {
                        onResult(Result.success(response.body()))
                    } else {
                        val errorJsonString = response.errorBody()?.string()
                        val message = try {
                            val jsonObject = JSONObject(errorJsonString.toString())
                            val error = jsonObject.optJSONObject("error")
                            error?.optString("message", "Unknown error")
                        } catch (e: Exception) {
                            "Error parsing response: ${e.message}"
                        }
                        onResult(Result.failure(Throwable(message)))
                    }
                }

                override fun onFailure(call: Call<UploadDocumentModel?>, t: Throwable) {
                    onResult(Result.failure(t))
                }
            }
        )
    }


    fun postFolder(
        folderName: String,
        onResult: (Result<CreateFolderModel?>) -> Unit
    ) {
        val body = mapOf("folderName" to folderName)
        val call = RetrofitConfig.ApiService.postFolder(
            "Bearer ${RetrofitConfig.token}",
            body
        )
        call.enqueue(object : Callback<CreateFolderModel?> {
            override fun onResponse(
                call: Call<CreateFolderModel?>,
                response: Response<CreateFolderModel?>
            ) {
                if (response.isSuccessful) {
                    onResult(Result.success(response.body()))
                } else {
                    val errorJsonString = response.errorBody()?.string()
                    val message = try {
                        val jsonObject = JSONObject(errorJsonString.toString())
                        jsonObject.optString("message", "Unknown error")
                    } catch (e: Exception) {
                        "Error parsing response: ${e.message}"
                    }
                    onResult(Result.failure(Throwable(message)))
                }
            }

            override fun onFailure(call: Call<CreateFolderModel?>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

}

