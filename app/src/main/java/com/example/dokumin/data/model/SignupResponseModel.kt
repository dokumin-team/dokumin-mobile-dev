package com.example.dokumin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignupResponseModel(
    var error: Boolean?,
    var message: String?,
    var success: Boolean?,
    var token: String?
) : Parcelable