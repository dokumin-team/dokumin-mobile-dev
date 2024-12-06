package com.example.dokumin.data.source.remote

import com.example.dokumin.data.model.requests.ResendOtpRequest
import com.example.dokumin.data.model.requests.SignInRequest
import com.example.dokumin.data.model.responses.SignupModel
import com.example.dokumin.data.model.requests.SignupRequest
import com.example.dokumin.data.model.requests.VerifyOtpRequest
import com.example.dokumin.data.model.responses.SigninModel
import com.example.dokumin.data.model.responses.VerifyOtpModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("users/signup")
    fun signUpUser(
        @Body registerRequest: SignupRequest
    ):Call<SignupModel?>

    fun signInUser(
        @Body loginRequest: SignInRequest
    ):Call<SigninModel?>

    @POST("userOTPVerifications/verify")
    fun verifyOtp(
        @Header("Authorization") token: String,
        @Body otp: VerifyOtpRequest
    ) :Call<VerifyOtpModel?>

    @POST("userOTPVerifications/resend")
    fun resendOtp(
        @Header("Authorization") token: String,
        @Body email: ResendOtpRequest
    ):Call<VerifyOtpModel?>


}