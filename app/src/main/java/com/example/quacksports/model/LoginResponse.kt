package com.example.quacksports.model

data class LoginResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String
)
