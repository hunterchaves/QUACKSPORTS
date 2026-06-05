package com.example.quacksports.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import com.example.quacksports.util.SessionManager
import com.example.quacksports.model.User
import com.example.quacksports.util.FirebaseRealtimeDatabaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseRealtimeDatabaseManager.getDatabaseInstance()
    private val sessionManager = SessionManager(application)

    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")

    var registerFirstName by mutableStateOf("")
    var registerLastName by mutableStateOf("")
    var registerEmail by mutableStateOf("")
    var registerPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    var currentUserData by mutableStateOf<User?>(null)

    init {
        auth.currentUser?.let {
            fetchUserData(it.uid)
        }
    }

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun login(onSuccess: () -> Unit) {
        if (loginEmail.isBlank() || loginPassword.isBlank()) {
            errorMessage = "Por favor, preencha todos os campos"
            return
        }

        isLoading = true
        errorMessage = null
        
        auth.signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    if (uid != null) {
                        fetchUserData(uid)
                    }
                    isLoading = false
                    isSuccess = true
                    onSuccess()
                } else {
                    isLoading = false
                    errorMessage = "Erro ao fazer login: ${task.exception?.message}"
                }
            }
    }

    fun register(onSuccess: () -> Unit) {
        if (registerEmail.isBlank() || registerPassword.isBlank() || registerFirstName.isBlank()) {
            errorMessage = "Por favor, preencha todos os campos obrigatórios"
            return
        }

        isLoading = true
        errorMessage = null

        auth.createUserWithEmailAndPassword(registerEmail, registerPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    if (uid != null) {
                        saveUserToDatabase(uid, registerFirstName, registerLastName, registerEmail, onSuccess)
                    } else {
                        isLoading = false
                        isSuccess = true
                        onSuccess()
                    }
                } else {
                    isLoading = false
                    errorMessage = "Erro ao cadastrar: ${task.exception?.message}"
                }
            }
    }

    private fun saveUserToDatabase(uid: String, firstName: String, lastName: String, email: String, onSuccess: () -> Unit) {
        val user = User(id = uid, firstName = firstName, lastName = lastName, email = email)
        database.getReference("users").child(uid).setValue(user)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    currentUserData = user
                    isSuccess = true
                    onSuccess()
                } else {
                    errorMessage = "Erro ao salvar dados: ${task.exception?.message}"
                }
            }
    }

    private fun fetchUserData(uid: String) {
        database.getReference("users").child(uid).get()
            .addOnSuccessListener { snapshot ->
                currentUserData = snapshot.getValue(User::class.java)
            }
    }

    fun logout() {
        auth.signOut()
        currentUserData = null
        sessionManager.logout()
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    if (firebaseUser != null) {
                        checkAndCreateUser(firebaseUser.uid, firebaseUser.displayName ?: "", "", firebaseUser.email ?: "", onSuccess)
                    } else {
                        isLoading = false
                        isSuccess = true
                        onSuccess()
                    }
                } else {
                    isLoading = false
                    errorMessage = "Erro Google: ${task.exception?.message}"
                }
            }
    }

    fun signInWithFacebook(accessToken: String, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null
        val credential = FacebookAuthProvider.getCredential(accessToken)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    if (firebaseUser != null) {
                        checkAndCreateUser(firebaseUser.uid, firebaseUser.displayName ?: "", "", firebaseUser.email ?: "", onSuccess)
                    } else {
                        isLoading = false
                        isSuccess = true
                        onSuccess()
                    }
                } else {
                    isLoading = false
                    errorMessage = "Erro Facebook: ${task.exception?.message}"
                }
            }
    }

    private fun checkAndCreateUser(uid: String, displayName: String, lastName: String, email: String, onSuccess: () -> Unit) {
        database.getReference("users").child(uid).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    currentUserData = snapshot.getValue(User::class.java)
                    isLoading = false
                    isSuccess = true
                    onSuccess()
                } else {
                    // Create basic user entry
                    val nameParts = displayName.split(" ", limit = 2)
                    val first = nameParts.getOrNull(0) ?: displayName
                    val last = nameParts.getOrNull(1) ?: ""
                    saveUserToDatabase(uid, first, last, email, onSuccess)
                }
            }
            .addOnFailureListener {
                // If we can't check, just try to fetch anyway or continue
                isLoading = false
                isSuccess = true
                onSuccess()
            }
    }
}
