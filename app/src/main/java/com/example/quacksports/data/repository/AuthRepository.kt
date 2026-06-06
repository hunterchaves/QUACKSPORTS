package com.example.quacksports.data.repository

import com.example.quacksports.data.firebase.FirestoreProvider
import com.example.quacksports.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirestoreProvider.db
) {
    private fun users() = db.collection("users")

    suspend fun login(email: String, password: String): User {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user!!.uid
        return fetchUser(uid) ?: User(id = uid, email = email)
    }

    suspend fun register(firstName: String, lastName: String, email: String, password: String): User {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user!!.uid
        val user = User(id = uid, firstName = firstName, lastName = lastName, email = email,
            role = "USER", createdAt = System.currentTimeMillis())
        users().document(uid).set(user).await()
        return user
    }

    suspend fun signInWithCredentialUser(uid: String, displayName: String, email: String): User {
        val existing = fetchUser(uid)
        if (existing != null) return existing
        val parts = displayName.split(" ", limit = 2)
        val user = User(id = uid, firstName = parts.getOrElse(0) { displayName },
            lastName = parts.getOrElse(1) { "" }, email = email, role = "USER",
            createdAt = System.currentTimeMillis())
        users().document(uid).set(user).await()
        return user
    }

    suspend fun fetchUser(uid: String): User? =
        users().document(uid).get().await().toObject(User::class.java)?.copy(id = uid)

    suspend fun setRole(uid: String, role: String, companyId: String) {
        users().document(uid).update(mapOf("role" to role, "companyId" to companyId)).await()
    }

    fun currentUid(): String? = auth.currentUser?.uid
    fun isLoggedIn(): Boolean = auth.currentUser != null
    fun logout() = auth.signOut()
}
