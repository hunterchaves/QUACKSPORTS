package com.example.quacksports.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Venue(
    val id: String = "",
    val companyId: String = "",
    val name: String = "",
    val addressLine: String = "",
    val city: String = "",
    val state: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val photos: List<String> = emptyList(),
    val ratingAvg: Double = 0.0
) {
    @get:Exclude
    val imageUrl: String get() = photos.firstOrNull() ?: ""
}
