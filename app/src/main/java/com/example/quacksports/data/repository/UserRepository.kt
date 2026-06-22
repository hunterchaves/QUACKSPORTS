package com.example.quacksports.data.repository

import com.example.quacksports.data.firebase.FirestoreProvider
import com.example.quacksports.model.Address
import com.example.quacksports.model.Card
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository(private val db: FirebaseFirestore = FirestoreProvider.db) {
    private fun userDoc(uid: String) = db.collection("users").document(uid)

    suspend fun updatePhotoPath(uid: String, path: String) {
        userDoc(uid).update("photoPath", path).await()
    }

    fun addresses(uid: String): Flow<List<Address>> = callbackFlow {
        val reg = userDoc(uid).collection("addresses").addSnapshotListener { snap, err ->
            if (err != null) { trySend(emptyList()); close(); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull {
                it.toObject(Address::class.java)?.copy(id = it.id)
            } ?: emptyList())
        }
        awaitClose { reg.remove() }
    }

    suspend fun addAddress(uid: String, address: Address) {
        val ref = userDoc(uid).collection("addresses").document()
        userDoc(uid).collection("addresses").document(ref.id).set(address.copy(id = ref.id)).await()
    }

    suspend fun deleteAddress(uid: String, addressId: String) {
        userDoc(uid).collection("addresses").document(addressId).delete().await()
    }

    fun cards(uid: String): Flow<List<Card>> = callbackFlow {
        val reg = userDoc(uid).collection("cards").addSnapshotListener { snap, err ->
            if (err != null) { trySend(emptyList()); close(); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull {
                it.toObject(Card::class.java)?.copy(id = it.id)
            } ?: emptyList())
        }
        awaitClose { reg.remove() }
    }

    suspend fun addCard(uid: String, card: Card) {
        val ref = userDoc(uid).collection("cards").document()
        userDoc(uid).collection("cards").document(ref.id).set(card.copy(id = ref.id)).await()
    }

    suspend fun deleteCard(uid: String, cardId: String) {
        userDoc(uid).collection("cards").document(cardId).delete().await()
    }
}
