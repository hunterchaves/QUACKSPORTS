package com.example.quacksports.data.repository

import com.example.quacksports.data.firebase.FirestoreProvider
import com.example.quacksports.model.Court
import com.example.quacksports.model.Venue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class VenueRepository(private val db: FirebaseFirestore = FirestoreProvider.db) {
    private fun venues() = db.collection("venues")

    fun allVenues(): Flow<List<Venue>> = callbackFlow {
        val reg = venues().addSnapshotListener { snap, err ->
            if (err != null) { trySend(emptyList()); close(); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull {
                it.toObject(Venue::class.java)?.copy(id = it.id)
            } ?: emptyList())
        }
        awaitClose { reg.remove() }
    }

    fun venuesByCompany(companyId: String): Flow<List<Venue>> = callbackFlow {
        val reg = venues().whereEqualTo("companyId", companyId).addSnapshotListener { snap, err ->
            if (err != null) { trySend(emptyList()); close(); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull {
                it.toObject(Venue::class.java)?.copy(id = it.id)
            } ?: emptyList())
        }
        awaitClose { reg.remove() }
    }

    suspend fun getVenue(venueId: String): Venue? =
        venues().document(venueId).get().await().toObject(Venue::class.java)?.copy(id = venueId)

    fun courts(venueId: String): Flow<List<Court>> = callbackFlow {
        val reg = venues().document(venueId).collection("courts").addSnapshotListener { snap, err ->
            if (err != null) { trySend(emptyList()); close(); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull {
                it.toObject(Court::class.java)?.copy(id = it.id, venueId = venueId)
            } ?: emptyList())
        }
        awaitClose { reg.remove() }
    }

    suspend fun getCourt(venueId: String, courtId: String): Court? =
        venues().document(venueId).collection("courts").document(courtId).get().await()
            .toObject(Court::class.java)?.copy(id = courtId, venueId = venueId)

    suspend fun upsertVenue(venue: Venue): String {
        val ref = if (venue.id.isBlank()) venues().document() else venues().document(venue.id)
        ref.set(venue.copy(id = ref.id)).await()
        return ref.id
    }

    suspend fun upsertCourt(venueId: String, court: Court): String {
        val col = venues().document(venueId).collection("courts")
        val ref = if (court.id.isBlank()) col.document() else col.document(court.id)
        ref.set(court.copy(id = ref.id, venueId = venueId)).await()
        return ref.id
    }
}
