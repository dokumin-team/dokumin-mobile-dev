package com.example.dokumin.data.model.requests

data class ResendOtpRequest (
    var email: String,
)

data class VerifyOtpRequest (
    var OTP: String,
)