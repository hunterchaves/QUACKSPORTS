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

    /** Default role for a given email: master-admin emails always land as ADMIN. */
    private fun defaultRoleFor(email: String): String =
        if (isAdminEmail(email)) "ADMIN" else "USER"

    suspend fun login(email: String, password: String): User {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user!!.uid
        return ensureRole(uid, email, fetchUser(uid))
    }

    suspend fun register(firstName: String, lastName: String, email: String, password: String): User {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user!!.uid
        val user = User(id = uid, firstName = firstName, lastName = lastName, email = email,
            role = defaultRoleFor(email), createdAt = System.currentTimeMillis())
        users().document(uid).set(user).await()
        return user
    }

    suspend fun signInWithCredentialUser(uid: String, displayName: String, email: String): User {
        val existing = fetchUser(uid)
        if (existing != null) return ensureRole(uid, email, existing)
        val parts = displayName.split(" ", limit = 2)
        val user = User(id = uid, firstName = parts.getOrElse(0) { displayName },
            lastName = parts.getOrElse(1) { "" }, email = email, role = defaultRoleFor(email),
            createdAt = System.currentTimeMillis())
        users().document(uid).set(user).await()
        return user
    }

    /**
     * Guarantees an allowlisted master-admin email always has role ADMIN, even if the
     * user doc was created before being added to the allowlist (or is missing).
     */
    private suspend fun ensureRole(uid: String, email: String, existing: User?): User {
        if (existing == null) {
            val u = User(id = uid, email = email, role = defaultRoleFor(email),
                createdAt = System.currentTimeMillis())
            users().document(uid).set(u).await()
            return u
        }
        if (isAdminEmail(email) && existing.role != "ADMIN") {
            users().document(uid).update("role", "ADMIN").await()
            return existing.copy(role = "ADMIN")
        }
        return existing
    }

    suspend fun fetchUser(uid: String): User? =
        users().document(uid).get().await().toObject(User::class.java)?.copy(id = uid)

    suspend fun setRole(uid: String, role: String, companyId: String) {
        users().document(uid).update(mapOf("role" to role, "companyId" to companyId)).await()
    }

    fun currentUid(): String? = auth.currentUser?.uid
    fun isLoggedIn(): Boolean = auth.currentUser != null
    fun logout() = auth.signOut()

    companion object {
        /** Emails that always get the ADMIN (product owner) role on register/login. */
        private val ADMIN_EMAILS = setOf("emanuelchaves199@gmail.com")

        fun isAdminEmail(email: String?): Boolean =
            email?.trim()?.lowercase() in ADMIN_EMAILS
    }
}
