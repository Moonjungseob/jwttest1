package com.busanit501.myapplication6.models

data class AuthenticationResponse (
    val jwt: String,
    val refreshToken:String

)