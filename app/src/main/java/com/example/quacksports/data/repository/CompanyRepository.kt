package com.example.quacksports.data.repository

import com.example.quacksports.data.firebase.FirestoreProvider
import com.example.quacksports.model.Company
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CompanyRepository(private val db: FirebaseFirestore = FirestoreProvider.db) {
    private fun companies() = db.collection("companies")

    fun all(): Flow<List<Company>> = callbackFlow {
        val reg = companies().addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull {
                it.toObject(Company::class.java)?.copy(id = it.id)
            } ?: emptyList())
        }
        awaitClose { reg.remove() }
    }

    suspend fun get(companyId: String): Company? =
        companies().document(companyId).get().await().toObject(Company::class.java)?.copy(id = companyId)

    suspend fun setOwner(companyId: String, ownerUid: String) {
        companies().document(companyId).update("ownerUid", ownerUid).await()
    }
}
