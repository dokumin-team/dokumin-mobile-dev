package com.example.dokumin.data.source.remote.datasource

import com.example.dokumin.data.model.responses.document.CountDocumentResponse
import com.example.dokumin.data.model.responses.document.ListDocumentModel
import com.example.dokumin.data.source.remote.RetrofitConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DocumentRemoteDataSource {

    fun getDocument(
        onResult: (Result<ListDocumentModel?>) -> Unit,
    ) {
        val call = RetrofitConfig.ApiService.getDocument(
            "Bearer ${RetrofitConfig.token}"
        )
        call.enqueue(
            object : Callback<ListDocumentModel?> {
                override fun onResponse(
                    call: Call<ListDocumentModel?>,
                    response: Response<ListDocumentModel?>
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

                override fun onFailure(call: Call<ListDocumentModel?>, t: Throwable) {
                    onResult(Result.failure(t))
                }
            }
        )
    }

    fun getNewestDocument(
        onResult: (Result<ListDocumentModel?>) -> Unit,
    ) {
        val call = RetrofitConfig.ApiService.getNewestDocument(
            "Bearer ${RetrofitConfig.token}"
        )
        call.enqueue(
            object : Callback<ListDocumentModel?> {
                override fun onResponse(
                    call: Call<ListDocumentModel?>,
                    response: Response<ListDocumentModel?>
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

                override fun onFailure(call: Call<ListDocumentModel?>, t: Throwable) {
                    onResult(Result.failure(t))
                }
            }
        )
    }

    fun getCountDocument(
        onResult: (Result<CountDocumentResponse?>) -> Unit,
    ) {
        val call = RetrofitConfig.ApiService.getCountDocument(
            "Bearer ${RetrofitConfig.token}"
        )
        call.enqueue(
            object : Callback<CountDocumentResponse?> {
                override fun onResponse(
                    call: Call<CountDocumentResponse?>,
                    response: Response<CountDocumentResponse?>
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

                override fun onFailure(call: Call<CountDocumentResponse?>, t: Throwable) {
                    onResult(Result.failure(t))
                }
            }
        )
    }
}