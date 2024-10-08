package com.busanit501.myapplication6.network


import com.busanit501.myapplication6.models.AuthenticationRequest
import com.busanit501.myapplication6.models.AuthenticationResponse
import com.busanit501.myapplication6.models.User
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {

    @POST("/api/users")
    fun createUser(@Body user: User): Call<User>


    @GET("/api/users")
    fun getAllUsers(): Call<List<User>>

    @GET("/api/users/{id}")
    fun getUserById(@Path("id") id: Long): Call<User>

    @POST("/api/authenticate")
    fun loginUser(@Body authenticationRequest: AuthenticationRequest): Call<AuthenticationResponse>

}