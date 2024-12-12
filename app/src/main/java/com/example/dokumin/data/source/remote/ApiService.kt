package com.example.dokumin.data.source.remote

import com.example.dokumin.data.model.requests.ResendOtpRequest
import com.example.dokumin.data.model.requests.SignInRequest
import com.example.dokumin.data.model.requests.SignupRequest
import com.example.dokumin.data.model.requests.VerifyOtpRequest
import com.example.dokumin.data.model.responses.auth.SigninModel
import com.example.dokumin.data.model.responses.auth.SignupModel
import com.example.dokumin.data.model.responses.auth.VerifyOtpModel
import com.example.dokumin.data.model.responses.document.CountDocumentResponse
import com.example.dokumin.data.model.responses.document.ListDocumentModel
import com.example.dokumin.data.model.responses.document.ScanDocumentResponse
import com.example.dokumin.data.model.responses.folder.CountFolderResponse
import com.example.dokumin.data.model.responses.folder.CreateFolderModel
import com.example.dokumin.data.model.responses.folder.ListFolderModel
import com.example.dokumin.data.model.responses.folder.UploadDocumentModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiService {


    // AUTH
    @POST("users/signup")
    fun signUpUser(
        @Body registerRequest: SignupRequest
    ):Call<SignupModel?>

    @POST("users/signin")
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


    // FOLDERS
    @GET("folders/getListFolder")
    fun getFolders(
        @Header("Authorization") token: String
    ):Call<ListFolderModel?>

    @Multipart
    @POST("folders/{folderId}/upload")
    fun postDocumentToFolder(
        @Header("Authorization") token: String,
        @Path("folderId") folderId: String,
        @Part("fileName") filename: String,
        @Part file: MultipartBody.Part?
    ): Call<UploadDocumentModel?>

    @POST("folders/create")
    fun postFolder(
        @Header("Authorization") auth: String,
        @Body body: Map<String, String>
    ): Call<CreateFolderModel>

    // DOCUMENTS
    @GET("documents/list")
    fun getDocument(
        @Header("Authorization") token: String
    ):Call<ListDocumentModel?>

    @GET("documents/newest")
    fun getNewestDocument(
        @Header("Authorization") token: String
    ):Call<ListDocumentModel?>

    @GET("documents/countDocument")
    fun getCountDocument(
        @Header("Authorization") token: String
    ):Call<CountDocumentResponse?>

    @GET("documents/countFolder")
    fun getCountFolder(
        @Header("Authorization") token: String
    ):Call<CountFolderResponse?>

    @GET("documents/listDocumentFolder/{folderId}")
    fun getDocumentByFolder(
        @Header("Authorization") token: String,
        @Path("folderId") folderId: String
    ):Call<ListDocumentModel?>

    @Multipart
    @POST("model/scan")
    fun postScannedDocument(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part?
    ): Call<ScanDocumentResponse?>



}