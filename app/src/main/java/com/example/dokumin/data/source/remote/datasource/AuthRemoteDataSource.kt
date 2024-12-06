package com.example.dokumin.data.source.remote.datasource

import com.example.dokumin.data.model.requests.ResendOtpRequest
import com.example.dokumin.data.model.requests.SignInRequest
import com.example.dokumin.data.model.responses.SignupModel
import com.example.dokumin.data.model.requests.SignupRequest
import com.example.dokumin.data.model.requests.VerifyOtpRequest
import com.example.dokumin.data.source.remote.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthRemoteDataSource {

    fun doSignUp(
        registerRequest: SignupRequest,
        onResult: (Result<SignupModel?>) -> Unit
    ) {
        val call = RetrofitConfig.ApiService.signUpUser(registerRequest)
        call.enqueue(object : Callback<SignupModel?> {
            override fun onResponse(call: Call<SignupModel?>, response: Response<SignupModel?>) {
                if (response.isSuccessful) {
                    onResult(Result.success(response.body()))
                } else {
                    onResult(Result.failure(Throwable(response.message())))
                }
            }

            override fun onFailure(call: Call<SignupModel?>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

    // Login
    fun doSignIn(
        loginRequest: SignInRequest,
        onResult: (Result<SignupModel?>) -> Unit
    ) {
        val call = RetrofitConfig.ApiService.signInUser(loginRequest)
        call.enqueue(object : Callback<SignupModel?> {
            override fun onResponse(call: Call<SignupModel?>, response: Response<SignupModel?>) {
                if (response.isSuccessful) {
                    onResult(Result.success(response.body()))
                } else {
                    onResult(Result.failure(Throwable(response.message())))
                }
            }

            override fun onFailure(call: Call<SignupModel?>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

    // Send OTP
    fun doVerifyOtp(
        otp: String,
        onResult: (Result<SignupModel?>) -> Unit
    ) {
        val call = RetrofitConfig.ApiService.verifyOtp(
            VerifyOtpRequest(otp)
        )
        call.enqueue(object : Callback<SignupModel?> {
            override fun onResponse(call: Call<SignupModel?>, response: Response<SignupModel?>) {
                if (response.isSuccessful) {
                    onResult(Result.success(response.body()))
                } else {
                    onResult(Result.failure(Throwable(response.message())))
                }
            }

            override fun onFailure(call: Call<SignupModel?>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }


    // Resend OTP
    fun doResendOtp(
        email: String,
        onResult: (Result<SignupModel?>) -> Unit
    ) {
        val call = RetrofitConfig.ApiService.resendOtp(
            ResendOtpRequest(email)
        )
        call.enqueue(object : Callback<SignupModel?> {
            override fun onResponse(call: Call<SignupModel?>, response: Response<SignupModel?>) {
                if (response.isSuccessful) {
                    onResult(Result.success(response.body()))
                } else {
                    onResult(Result.failure(Throwable(response.message())))
                }
            }

            override fun onFailure(call: Call<SignupModel?>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

}