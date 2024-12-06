package com.example.dokumin.data.source.remote

import com.example.dokumin.data.source.remote.requests.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users/signup")
    fun registerUser(
        @Body registerRequest: RegisterRequest
    ):Call<String>
}