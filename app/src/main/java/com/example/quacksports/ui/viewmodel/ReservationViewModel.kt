package com.example.quacksports.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.ReservationRepository
import com.example.quacksports.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val reservationRepository = ReservationRepository()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var reservationSuccess by mutableStateOf(false)

    private val _userReservations = MutableStateFlow<List<Reservation>>(emptyList())
    val userReservations: StateFlow<List<Reservation>> = _userReservations.asStateFlow()

    init {
        auth.currentUser?.uid?.let {
            fetchUserReservations(it)
        }
    }

    fun createReservation(venueId: String, venueTitle: String, date: String, time: String, price: String, onSuccess: () -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            errorMessage = "Usuário não autenticado."
            return
        }

        isLoading = true
        errorMessage = null
        reservationSuccess = false

        val reservation = Reservation(
            venueId = venueId,
            userId = userId,
            venueTitle = venueTitle,
            date = date,
            time = time,
            price = price
        )

        reservationRepository.createReservation(reservation,
            onSuccess = {
                isLoading = false
                reservationSuccess = true
                onSuccess()
                fetchUserReservations(userId) // Refresh reservations after creation
            },
            onFailure = { message ->
                isLoading = false
                errorMessage = message
                reservationSuccess = false
            }
        )
    }

    fun fetchUserReservations(userId: String) {
        viewModelScope.launch {
            reservationRepository.getUserReservations(userId)
                .catch { exception ->
                    errorMessage = "Erro ao buscar reservas: ${exception.message}"
                }
                .collect {
                    _userReservations.value = it
                }
        }
    }
}
