package com.example.dokumin.data.model.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignupModel(
    var error: Boolean?,
    var message: String?,
    var success: Boolean?,
    var token: String?
) : Parcelable