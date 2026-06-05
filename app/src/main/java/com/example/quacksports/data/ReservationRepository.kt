package com.example.quacksports.data

import com.example.quacksports.model.Reservation
import com.example.quacksports.util.FirebaseRealtimeDatabaseManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ReservationRepository {

    private val database = FirebaseRealtimeDatabaseManager.getDatabaseInstance()
    private val reservationsRef = database.getReference("reservations")

    fun createReservation(reservation: Reservation, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val reservationId = reservationsRef.push().key
        if (reservationId == null) {
            onFailure("Não foi possível gerar um ID para a reserva.")
            return
        }
        val newReservation = reservation.copy(id = reservationId)
        reservationsRef.child(reservationId).setValue(newReservation)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Erro desconhecido ao criar reserva.")
            }
    }

    fun getUserReservations(userId: String): Flow<List<Reservation>> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reservations = snapshot.children.mapNotNull { dataSnapshot ->
                    dataSnapshot.getValue(Reservation::class.java)?.copy(id = dataSnapshot.key ?: "")
                }.filter { it.userId == userId } // Filter by userId
                trySend(reservations).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        reservationsRef.addValueEventListener(valueEventListener)
        awaitClose { reservationsRef.removeEventListener(valueEventListener) }
    }
}
