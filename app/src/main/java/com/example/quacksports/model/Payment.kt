package com.example.quacksports.model

data class Payment(
    val id: String = "",
    val userId: String = "",
    val reservationId: String = "",
    val cardId: String = "",
    val amount: Double = 0.0,
    val status: String = "APPROVED",
    val createdAt: Long = 0L
)
