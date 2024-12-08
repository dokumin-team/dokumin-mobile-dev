package com.example.dokumin.data.source.remote.datasource

import com.example.dokumin.data.model.responses.folder.ListFolderModel
import com.example.dokumin.data.source.remote.RetrofitConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FolderRemoteDataSource {

    fun getFolders(
        onResult: (Result<ListFolderModel?>) -> Unit,
    ){
        val call = RetrofitConfig.ApiService.getFolders(
            "Bearer ${RetrofitConfig.token}"
        )
        call.enqueue(
            object : Callback<ListFolderModel?> {
                override fun onResponse(
                    call: Call<ListFolderModel?>,
                    response: Response<ListFolderModel?>
                ) {
                    if(response.isSuccessful){
                        onResult(Result.success(response.body()))
                    }else{
                        val errorJsonString = response.errorBody()?.string()
                        val message = try{
                            val jsonObject = JSONObject(errorJsonString.toString())
                            jsonObject.optString("message", "Unknown error")
                        }catch (e: Exception){
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

}