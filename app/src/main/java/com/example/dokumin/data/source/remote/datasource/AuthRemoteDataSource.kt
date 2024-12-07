package com.example.dokumin.data.source.remote.datasource

import android.util.Log
import com.example.dokumin.data.model.requests.ResendOtpRequest
import com.example.dokumin.data.model.requests.SignInRequest
import com.example.dokumin.data.model.responses.SignupModel
import com.example.dokumin.data.model.requests.SignupRequest
import com.example.dokumin.data.model.requests.VerifyOtpRequest
import com.example.dokumin.data.model.responses.SigninModel
import com.example.dokumin.data.model.responses.VerifyOtpModel
import com.example.dokumin.data.source.remote.RetrofitConfig
import org.json.JSONObject
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
                    val errorJsonString = response.errorBody()?.string()
                    val message =try {
                        val jsonObject = JSONObject(errorJsonString.toString()) // Parse the JSON string
                        jsonObject.optString("message", "Unknown error") // Extract "message" or provide default
                    } catch (e: Exception) {
                        "Error parsing response: ${e.message}" // Handle parsing error
                    }
                    onResult(Result.failure(Throwable(message)))
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
        onResult: (Result<SigninModel?>) -> Unit
    ) {
        val call = RetrofitConfig.ApiService.signInUser(loginRequest)
        call.enqueue(object : Callback<SigninModel?> {
            override fun onResponse(call: Call<SigninModel?>, response: Response<SigninModel?>) {
                if (response.isSuccessful) {
                    onResult(Result.success(response.body()))
                } else {
                    val errorJsonString = response.errorBody()?.string()
                    val message =try {
                        val jsonObject = JSONObject(errorJsonString.toString()) // Parse the JSON string
                        jsonObject.optString("message", "Unknown error") // Extract "message" or provide default
                    } catch (e: Exception) {
                        "Error parsing response: ${e.message}" // Handle parsing error
                    }
                    onResult(Result.failure(Throwable(message)))
                }
            }

            override fun onFailure(call: Call<SigninModel?>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

    // Send OTP
    fun doVerifyOtp(
        otp: String,
        onResult: (Result<VerifyOtpModel?>) -> Unit
    ) {
        val call = RetrofitConfig.ApiService.verifyOtp(
            "Bearer ${RetrofitConfig.token}",
            VerifyOtpRequest(otp)
        )
        call.enqueue(object : Callback<VerifyOtpModel?> {
            override fun onResponse(call: Call<VerifyOtpModel?>, response: Response<VerifyOtpModel?>) {
                if (response.isSuccessful) {
                    onResult(Result.success(response.body()))
                } else {
                    val errorJsonString = response.errorBody()?.string()
                    val message =try {
                        val jsonObject = JSONObject(errorJsonString.toString()) // Parse the JSON string
                        jsonObject.optString("message", "Unknown error") // Extract "message" or provide default
                    } catch (e: Exception) {
                        "Error parsing response: ${e.message}" // Handle parsing error
                    }
                    onResult(Result.failure(Throwable(message)))
                }
            }

            override fun onFailure(call: Call<VerifyOtpModel?>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }


    // Resend OTP
    fun doResendOtp(
        email: String,
        onResult: (Result<VerifyOtpModel?>) -> Unit
    ) {
        val call = RetrofitConfig.ApiService.resendOtp(
            "Bearer ${RetrofitConfig.token}",
            ResendOtpRequest(email)
        )
        call.enqueue(object : Callback<VerifyOtpModel?> {
            override fun onResponse(call: Call<VerifyOtpModel?>, response: Response<VerifyOtpModel?>) {
                if (response.isSuccessful) {
                    onResult(Result.success(response.body()))
                } else {
                    val errorJsonString = response.errorBody()?.string()
                    val message =try {
                        val jsonObject = JSONObject(errorJsonString.toString()) // Parse the JSON string
                        jsonObject.optString("message", "Unknown error") // Extract "message" or provide default
                    } catch (e: Exception) {
                        "Error parsing response: ${e.message}" // Handle parsing error
                    }
                    onResult(Result.failure(Throwable(message)))
                }
            }

            override fun onFailure(call: Call<VerifyOtpModel?>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

}