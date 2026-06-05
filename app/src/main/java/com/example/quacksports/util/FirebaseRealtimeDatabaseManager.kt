package com.example.quacksports.util

import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

object FirebaseRealtimeDatabaseManager {
    fun initialize(app: FirebaseApp) {
        // Firebase Realtime Database is initialized by default when FirebaseApp is initialized
        // We can optionally set a custom database URL if needed, but google-services.json should handle it.
        // If there's a need to explicitly set a different URL, it would be like:
        // FirebaseDatabase.getInstance(app, "https://your-custom-url.firebaseio.com/").setPersistenceEnabled(true)
    }

    fun getDatabaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}
