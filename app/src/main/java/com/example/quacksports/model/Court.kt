package com.example.quacksports.model

data class Court(
    val id: String = "",
    val venueId: String = "",
    val name: String = "",
    val sport: String = Sport.MULTISPORT.name,
    val pricePerHour: Double = 0.0,
    val photos: List<String> = emptyList(),
    val openHour: Int = 8,
    val closeHour: Int = 22
)
