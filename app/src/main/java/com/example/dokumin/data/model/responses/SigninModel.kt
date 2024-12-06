package com.example.dokumin.data.model.responses

data class SigninModel(
    var `data`: Data?,
    var message: String?,
    var status: String?,
    var token: String?
)

data class Data(
    var email: String?,
    var id: String?,
    var name: String?,
    var verified: Boolean?
)