package com.example.quacksports.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.repository.AuthRepository
import com.example.quacksports.model.User
import com.example.quacksports.model.UserRole
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AuthRepository()
    private val auth = FirebaseAuth.getInstance()

    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")
    var registerFirstName by mutableStateOf("")
    var registerLastName by mutableStateOf("")
    var registerEmail by mutableStateOf("")
    var registerPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var currentUserData by mutableStateOf<User?>(null)

    val role: UserRole get() = UserRole.from(currentUserData?.role)

    init { auth.currentUser?.uid?.let { uid -> viewModelScope.launch { currentUserData = repo.fetchUser(uid) } } }

    fun isLoggedIn(): Boolean = repo.isLoggedIn()

    fun login(onSuccess: () -> Unit) {
        if (loginEmail.isBlank() || loginPassword.isBlank()) { errorMessage = "Preencha todos os campos"; return }
        isLoading = true; errorMessage = null
        viewModelScope.launch {
            try { currentUserData = repo.login(loginEmail, loginPassword); onSuccess() }
            catch (e: Exception) { errorMessage = "Erro ao fazer login: ${e.message}" }
            finally { isLoading = false }
        }
    }

    fun register(onSuccess: () -> Unit) {
        if (registerEmail.isBlank() || registerPassword.isBlank() || registerFirstName.isBlank()) {
            errorMessage = "Preencha os campos obrigatórios"; return
        }
        isLoading = true; errorMessage = null
        viewModelScope.launch {
            try { currentUserData = repo.register(registerFirstName, registerLastName, registerEmail, registerPassword); onSuccess() }
            catch (e: Exception) { errorMessage = "Erro ao cadastrar: ${e.message}" }
            finally { isLoading = false }
        }
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit) =
        socialSignIn(GoogleAuthProvider.getCredential(idToken, null), onSuccess)

    fun signInWithFacebook(accessToken: String, onSuccess: () -> Unit) =
        socialSignIn(FacebookAuthProvider.getCredential(accessToken), onSuccess)

    private fun socialSignIn(credential: com.google.firebase.auth.AuthCredential, onSuccess: () -> Unit) {
        isLoading = true; errorMessage = null
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fu = task.result?.user
                if (fu != null) viewModelScope.launch {
                    try { currentUserData = repo.signInWithCredentialUser(fu.uid, fu.displayName ?: "", fu.email ?: "") }
                    finally { isLoading = false; onSuccess() }
                } else { isLoading = false; onSuccess() }
            } else { isLoading = false; errorMessage = "Erro: ${task.exception?.message}" }
        }
    }

    fun setRole(role: UserRole, companyId: String, onDone: () -> Unit) {
        val uid = repo.currentUid() ?: return
        viewModelScope.launch {
            repo.setRole(uid, role.name, companyId)
            currentUserData = repo.fetchUser(uid)
            onDone()
        }
    }

    fun refresh() { repo.currentUid()?.let { uid -> viewModelScope.launch { currentUserData = repo.fetchUser(uid) } } }

    fun logout() { repo.logout(); currentUserData = null }
}
