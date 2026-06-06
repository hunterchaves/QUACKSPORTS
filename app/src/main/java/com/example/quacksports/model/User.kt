package com.example.quacksports.model

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: String = UserRole.USER.name,
    val photoPath: String = "",
    val companyId: String = "",
    val createdAt: Long = 0L
)
