package com.busanit501.myapplication6.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://10.100.201.6:8080/" // 실제 API URL로 변경

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }4

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}