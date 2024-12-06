package com.example.dokumin.data.source.remote

import com.example.dokumin.data.model.requests.ResendOtpRequest
import com.example.dokumin.data.model.requests.SignInRequest
import com.example.dokumin.data.model.responses.SignupModel
import com.example.dokumin.data.model.requests.SignupRequest
import com.example.dokumin.data.model.requests.VerifyOtpRequest
import com.example.dokumin.data.model.responses.SigninModel
import retrofit2.Call
import retrofit2.http.Body
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
        @Body otp: VerifyOtpRequest
    ) :Call<SignupModel?>

    @POST("userOTPVerifications/resend")
    fun resendOtp(
        @Body email: ResendOtpRequest
    ):Call<SignupModel?>


}