package com.example.quacksports.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.repository.UserRepository
import com.example.quacksports.model.Address
import com.example.quacksports.model.Card
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository()
    private val auth = FirebaseAuth.getInstance()
    private fun uid() = auth.currentUser?.uid

    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    init {
        uid()?.let { id ->
            viewModelScope.launch { repo.addresses(id).collect { _addresses.value = it } }
            viewModelScope.launch { repo.cards(id).collect { _cards.value = it } }
        }
    }

    fun setAvatar(uri: Uri, onDone: (String) -> Unit) {
        val id = uid() ?: return
        viewModelScope.launch {
            val path = withContext(Dispatchers.IO) {
                val ctx = getApplication<Application>()
                val dest = File(ctx.filesDir, "avatar_$id.jpg")
                ctx.contentResolver.openInputStream(uri)?.use { input ->
                    dest.outputStream().use { input.copyTo(it) }
                }
                dest.absolutePath
            }
            repo.updatePhotoPath(id, path)
            onDone(path)
        }
    }

    fun addAddress(a: Address) { uid()?.let { id -> viewModelScope.launch { repo.addAddress(id, a) } } }
    fun deleteAddress(addressId: String) { uid()?.let { id -> viewModelScope.launch { repo.deleteAddress(id, addressId) } } }
    fun addCard(c: Card) { uid()?.let { id -> viewModelScope.launch { repo.addCard(id, c) } } }
    fun deleteCard(cardId: String) { uid()?.let { id -> viewModelScope.launch { repo.deleteCard(id, cardId) } } }
}
