package com.example.dokumin.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dokumin.data.model.requests.SignInRequest
import com.example.dokumin.data.model.requests.SignupRequest
import com.example.dokumin.data.model.responses.auth.SigninModel
import com.example.dokumin.data.model.responses.auth.SignupModel
import com.example.dokumin.data.model.responses.auth.VerifyOtpModel
import com.example.dokumin.data.source.remote.datasource.AuthRemoteDataSource

object AuthRepository {

    var email: String = ""

    // Error message
    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: LiveData<String?> = _errorMessage


    private val _signInResponse: MutableLiveData<SigninModel?> = MutableLiveData()
    val signInResponse: LiveData<SigninModel?> = _signInResponse
    fun signIn(email: String, password: String) {
        // Call API
        val data = SignInRequest(email = email, password = password)
        AuthRemoteDataSource.doSignIn(
            data, { result ->
                if (result.isSuccess) {
                    _signInResponse.value = result.getOrNull()
                } else {
                    Log.d("SIGIN REPO", result.exceptionOrNull()?.message.toString())
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            }
        )
    }

    private val _signUpResponse: MutableLiveData<SignupModel?> = MutableLiveData()
    val signUpResponse: LiveData<SignupModel?> = _signUpResponse
    fun signUp(email: String, password: String, name: String) {
        // Call API
        val data = SignupRequest(
            email = email, password = password, name = name
        )
        AuthRemoteDataSource.doSignUp(
            data, { result ->
                if (result.isSuccess) {
                    _signUpResponse.value = result.getOrNull()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            }
        )
    }

    private val _verifyOtpResponse: MutableLiveData<VerifyOtpModel?> = MutableLiveData()
    val verifyOtpResponse: LiveData<VerifyOtpModel?> = _verifyOtpResponse
    fun verifyOtp(otp: String) {
        // Call API
        AuthRemoteDataSource.doVerifyOtp(
            otp, { result ->
                if (result.isSuccess) {
                    _verifyOtpResponse.value = result.getOrNull()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            }
        )
    }

    private val _resendOtpResponse: MutableLiveData<VerifyOtpModel?> = MutableLiveData()
    val resendOtpResponse: LiveData<VerifyOtpModel?> = _resendOtpResponse
    fun resendOtp(email: String) {
        // Call API
        AuthRemoteDataSource.doResendOtp(
            email, { result ->
                if (result.isSuccess) {
                    _resendOtpResponse.value = result.getOrNull()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            }
        )
    }


}