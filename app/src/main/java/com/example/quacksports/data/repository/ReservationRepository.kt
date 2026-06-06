package com.example.quacksports.data.repository

import com.example.quacksports.data.firebase.FirestoreProvider
import com.example.quacksports.model.Reservation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ReservationRepository(private val db: FirebaseFirestore = FirestoreProvider.db) {
    private fun reservations() = db.collection("reservations")

    suspend fun create(reservation: Reservation): String {
        val ref = reservations().document()
        reservations().document(ref.id).set(reservation.copy(id = ref.id)).await()
        return ref.id
    }

    suspend fun forCourtOnDate(courtId: String, date: String): List<Reservation> =
        reservations()
            .whereEqualTo("courtId", courtId)
            .whereEqualTo("date", date)
            .get().await()
            .documents.mapNotNull { it.toObject(Reservation::class.java)?.copy(id = it.id) }

    fun byUser(userId: String): Flow<List<Reservation>> =
        listenQuery(reservations().whereEqualTo("userId", userId))

    fun byCompany(companyId: String): Flow<List<Reservation>> =
        listenQuery(reservations().whereEqualTo("companyId", companyId))

    fun all(): Flow<List<Reservation>> = listenQuery(reservations())

    private fun listenQuery(query: Query): Flow<List<Reservation>> = callbackFlow {
        val reg = query.addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull {
                it.toObject(Reservation::class.java)?.copy(id = it.id)
            } ?: emptyList())
        }
        awaitClose { reg.remove() }
    }
}
