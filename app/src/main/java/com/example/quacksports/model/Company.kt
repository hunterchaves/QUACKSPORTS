package com.example.quacksports.model

data class Company(
    val id: String = "",
    val name: String = "",
    val ownerUid: String = "",
    val CNPJ: String = "",
    val email: String = "",
    val phone: String = "",
    val logoUrl: String = "",
    val description: String = "",
    val createdAt: Long = 0L
)
