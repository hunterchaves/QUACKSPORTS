package com.example.quacksports.model

data class Reservation(
    val id: String = "",
    val venueId: String = "",
    val userId: String = "",
    val venueTitle: String = "",
    val date: String = "",
    val time: String = "",
    val price: String = "",
    val status: String = "PENDING" // PENDING, CONFIRMED, CANCELLED
)
