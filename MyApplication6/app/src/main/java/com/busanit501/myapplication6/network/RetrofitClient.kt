package com.busanit501.myapplication6.network

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://10.100.201.6:8080/" // 실제 API URL로 변경

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val sharedPreferences = android.content.ContextWrapper.getApplicationContext().getSharedPreferences("MyAppPrefs", android.content.Context.MODE_PRIVATE)
            val jwt = sharedPreferences.getString("jwt", null)

            var request: Request = chain.request()
            if (jwt != null) {
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer $jwt")
                    .build()
            }

            chain.proceed(request)
        }
        .build()


    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}