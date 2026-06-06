package com.example.quacksports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.repository.ReservationRepository
import com.example.quacksports.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ReservationViewModel(
    private val repo: ReservationRepository = ReservationRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {
    private val _userReservations = MutableStateFlow<List<Reservation>>(emptyList())
    val userReservations: StateFlow<List<Reservation>> = _userReservations

    init { auth.currentUser?.uid?.let { load(it) } }

    fun load(userId: String) {
        viewModelScope.launch {
            repo.byUser(userId)
                .catch { }
                .collect { list -> _userReservations.value = list.sortedByDescending { it.createdAt } }
        }
    }
}
