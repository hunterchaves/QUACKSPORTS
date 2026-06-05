package com.example.quacksports.data

import com.example.quacksports.model.Venue
import com.example.quacksports.util.FirebaseRealtimeDatabaseManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VenueRepository {

    private val database = FirebaseRealtimeDatabaseManager.getDatabaseInstance()
    private val venuesRef = database.getReference("venues")

    fun getVenues(): Flow<List<Venue>> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val venues = snapshot.children.mapNotNull { dataSnapshot ->
                    dataSnapshot.getValue(Venue::class.java)?.copy(id = dataSnapshot.key ?: "")
                }
                trySend(venues).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        venuesRef.addValueEventListener(valueEventListener)
        awaitClose { venuesRef.removeEventListener(valueEventListener) }
    }
}
