package com.example.quacksports.data.repository

import com.example.quacksports.data.firebase.FirestoreProvider
import com.example.quacksports.model.Payment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class PaymentRepository(private val db: FirebaseFirestore = FirestoreProvider.db) {
    private fun payments() = db.collection("payments")

    suspend fun pay(userId: String, cardId: String, amount: Double): Payment {
        delay(1500)
        val ref = payments().document()
        val payment = Payment(id = ref.id, userId = userId, cardId = cardId, amount = amount,
            status = "APPROVED", createdAt = System.currentTimeMillis())
        payments().document(ref.id).set(payment).await()
        return payment
    }

    suspend fun linkReservation(paymentId: String, reservationId: String) {
        payments().document(paymentId).update("reservationId", reservationId).await()
    }

    suspend fun all(): List<Payment> =
        payments().get().await().documents.mapNotNull {
            it.toObject(Payment::class.java)?.copy(id = it.id)
        }
}
