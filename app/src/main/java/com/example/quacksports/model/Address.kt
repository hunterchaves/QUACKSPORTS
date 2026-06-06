package com.example.quacksports.model

data class Address(
    val id: String = "",
    val label: String = "",
    val street: String = "",
    val number: String = "",
    val complement: String = "",
    val city: String = "",
    val state: String = "",
    val zip: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)
