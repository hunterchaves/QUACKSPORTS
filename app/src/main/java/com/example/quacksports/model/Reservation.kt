package com.example.quacksports.model

data class Reservation(
    val id: String = "",
    val userId: String = "",
    val companyId: String = "",
    val venueId: String = "",
    val courtId: String = "",
    val courtName: String = "",
    val venueName: String = "",
    val sport: String = Sport.MULTISPORT.name,
    val date: String = "",
    val startHour: Int = 0,
    val endHour: Int = 0,
    val amount: Double = 0.0,
    val paymentId: String = "",
    val status: String = "CONFIRMED",
    val createdAt: Long = 0L
)
