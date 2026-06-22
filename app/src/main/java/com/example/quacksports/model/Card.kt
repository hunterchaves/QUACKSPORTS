package com.example.quacksports.model

data class Card(
    val id: String = "",
    val brand: String = "",
    val last4: String = "",
    val holderName: String = "",
    val expMonth: Int = 0,
    val expYear: Int = 0
)
