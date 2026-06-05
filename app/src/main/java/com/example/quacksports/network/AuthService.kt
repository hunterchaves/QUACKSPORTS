package com.example.quacksports.network

import com.example.quacksports.model.LoginRequest
import com.example.quacksports.model.LoginResponse
import com.example.quacksports.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>
}
