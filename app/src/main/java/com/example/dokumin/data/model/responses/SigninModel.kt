package com.example.dokumin.data.model.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SigninModel(
    var error: Boolean?,
    var message: String?,
    var success: Boolean?,
    var token: String?
) : Parcelable