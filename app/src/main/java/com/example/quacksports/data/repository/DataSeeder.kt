package com.example.quacksports.data.repository

import com.example.quacksports.data.firebase.FirestoreProvider
import com.example.quacksports.data.logic.SeedData
import com.example.quacksports.model.Company
import com.example.quacksports.model.Court
import com.example.quacksports.model.Venue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DataSeeder(private val db: FirebaseFirestore = FirestoreProvider.db) {
    suspend fun seed(): Int {
        val existing = db.collection("companies").limit(1).get().await()
        if (!existing.isEmpty) return 0

        val now = System.currentTimeMillis()
        SeedData.build().forEach { sc ->
            val companyRef = db.collection("companies").document()
            val company = Company(id = companyRef.id, name = sc.name, ownerUid = "",
                logoUrl = sc.logoUrl, description = sc.description, createdAt = now)
            companyRef.set(company).await()

            sc.venues.forEach { sv ->
                val venueRef = db.collection("venues").document()
                val venue = Venue(id = venueRef.id, companyId = companyRef.id, name = sv.name,
                    addressLine = sv.addressLine, city = sv.city, state = sv.state,
                    lat = sv.lat, lng = sv.lng, photos = sv.photos, ratingAvg = sv.ratingAvg)
                venueRef.set(venue).await()

                sv.courts.forEach { scrt ->
                    val courtRef = venueRef.collection("courts").document()
                    val court = Court(id = courtRef.id, venueId = venueRef.id, name = scrt.name,
                        sport = scrt.sport.name, pricePerHour = scrt.pricePerHour, photos = scrt.photos,
                        openHour = scrt.openHour, closeHour = scrt.closeHour)
                    courtRef.set(court).await()
                }
            }
        }
        return SeedData.build().size
    }
}
