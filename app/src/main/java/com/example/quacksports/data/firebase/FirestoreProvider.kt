package com.example.quacksports.data.firebase

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreProvider {
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
}
