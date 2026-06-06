package com.example.quacksports.data.repository

import com.example.quacksports.data.firebase.FirestoreProvider
import com.example.quacksports.data.logic.SeedData
import com.example.quacksports.model.Company
import com.example.quacksports.model.Court
import com.example.quacksports.model.Venue
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class DataSeeder(private val db: FirebaseFirestore = FirestoreProvider.db) {
    suspend fun seed(): Int {
        val now = System.currentTimeMillis()
        val seed = SeedData.build()
        seed.forEach { sc ->
            val companyRef = ensureCompany(sc, now)
            sc.venues.forEach { sv ->
                val venueRef = ensureVenue(companyRef.id, sv)
                sv.courts.forEach { scrt ->
                    ensureCourt(venueRef, scrt)
                }
            }
        }
        return seed.size
    }

    private suspend fun ensureCompany(sc: SeedData.SeedCompany, now: Long): DocumentReference {
        val existing = db.collection("companies")
            .whereEqualTo("name", sc.name)
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
        val ref = existing?.reference ?: db.collection("companies").document()
        val previous = existing?.toObject(Company::class.java)
        val company = Company(
            id = ref.id,
            name = sc.name,
            ownerUid = previous?.ownerUid ?: "",
            logoUrl = sc.logoUrl,
            description = sc.description,
            createdAt = previous?.createdAt?.takeIf { it > 0L } ?: now
        )
        ref.set(company, SetOptions.merge()).await()
        return ref
    }

    private suspend fun ensureVenue(companyId: String, sv: SeedData.SeedVenue): DocumentReference {
        val existing = db.collection("venues")
            .whereEqualTo("name", sv.name)
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
        val ref = existing?.reference ?: db.collection("venues").document()
        val venue = Venue(
            id = ref.id,
            companyId = companyId,
            name = sv.name,
            addressLine = sv.addressLine,
            city = sv.city,
            state = sv.state,
            lat = sv.lat,
            lng = sv.lng,
            photos = sv.photos,
            ratingAvg = sv.ratingAvg
        )
        ref.set(venue, SetOptions.merge()).await()
        return ref
    }

    private suspend fun ensureCourt(venueRef: DocumentReference, scrt: SeedData.SeedCourt) {
        val existing = venueRef.collection("courts")
            .whereEqualTo("name", scrt.name)
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
        val ref = existing?.reference ?: venueRef.collection("courts").document()
        val court = Court(
            id = ref.id,
            venueId = venueRef.id,
            name = scrt.name,
            sport = scrt.sport.name,
            pricePerHour = scrt.pricePerHour,
            photos = scrt.photos,
            openHour = scrt.openHour,
            closeHour = scrt.closeHour
        )
        ref.set(court, SetOptions.merge()).await()
    }
}
