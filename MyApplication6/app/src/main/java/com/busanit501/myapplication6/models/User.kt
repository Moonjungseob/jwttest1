package com.busanit501.myapplication6.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String
)