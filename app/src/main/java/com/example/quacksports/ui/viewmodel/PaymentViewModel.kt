package com.example.quacksports.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.repository.PaymentRepository
import com.example.quacksports.data.repository.ReservationRepository
import com.example.quacksports.data.repository.UserRepository
import com.example.quacksports.model.Card
import com.example.quacksports.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val paymentRepo: PaymentRepository = PaymentRepository(),
    private val reservationRepo: ReservationRepository = ReservationRepository(),
    private val userRepo: UserRepository = UserRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    var selectedCardId by mutableStateOf<String?>(null)
    var isProcessing by mutableStateOf(false); private set
    var success by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        auth.currentUser?.uid?.let { uid ->
            viewModelScope.launch { userRepo.cards(uid).collect { _cards.value = it; if (selectedCardId == null) selectedCardId = it.firstOrNull()?.id } }
        }
    }

    fun payAndReserve(draft: Reservation, onDone: () -> Unit) {
        val uid = auth.currentUser?.uid ?: run { errorMessage = "Faça login"; return }
        val cardId = selectedCardId ?: run { errorMessage = "Selecione um cartão"; return }
        isProcessing = true; errorMessage = null
        viewModelScope.launch {
            try {
                val payment = paymentRepo.pay(uid, cardId, draft.amount)
                val reservationId = reservationRepo.create(
                    draft.copy(userId = uid, paymentId = payment.id, status = "CONFIRMED",
                        createdAt = System.currentTimeMillis())
                )
                paymentRepo.linkReservation(payment.id, reservationId)
                success = true
                onDone()
            } catch (e: Exception) { errorMessage = "Falha no pagamento: ${e.message}" }
            finally { isProcessing = false }
        }
    }
}
