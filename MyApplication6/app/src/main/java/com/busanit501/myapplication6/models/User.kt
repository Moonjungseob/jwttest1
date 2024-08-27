package com.busanit501.myapplication6.models

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,  // 실제 비밀번호는 보통 클라이언트에서 저장하지 않음

    @SerializedName("role")
    val role: Int = 0  // 역할을 정수로 받아옴
) {
    // 역할을 문자열로 변환하는 메서드
    fun getRoleAsString(): String {
        return if (role == 1) "ROLE_ADMIN" else "ROLE_USER"
    }
}
