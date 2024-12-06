package com.example.dokumin.data.model.requests

data class VerifyOtpRequest (
    var email: String,
)

data class ResendOtpRequest (
    var OTP: String,
)