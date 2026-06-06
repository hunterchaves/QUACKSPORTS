package com.example.quacksports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.logic.Revenue
import com.example.quacksports.data.repository.CompanyRepository
import com.example.quacksports.data.repository.PaymentRepository
import com.example.quacksports.data.repository.ReservationRepository
import com.example.quacksports.data.repository.VenueRepository
import com.example.quacksports.model.Company
import com.example.quacksports.model.Payment
import com.example.quacksports.model.Reservation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val companyRepo: CompanyRepository = CompanyRepository(),
    private val reservationRepo: ReservationRepository = ReservationRepository(),
    private val venueRepo: VenueRepository = VenueRepository(),
    private val paymentRepo: PaymentRepository = PaymentRepository()
) : ViewModel() {
    private val _companies = MutableStateFlow<List<Company>>(emptyList())
    val companies: StateFlow<List<Company>> = _companies
    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    val reservations: StateFlow<List<Reservation>> = _reservations
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments
    private val _venueCount = MutableStateFlow(0)
    val venueCount: StateFlow<Int> = _venueCount
    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue: StateFlow<Double> = _totalRevenue
    private val _revenuePerCompany = MutableStateFlow<Map<String, Double>>(emptyMap())
    val revenuePerCompany: StateFlow<Map<String, Double>> = _revenuePerCompany

    init {
        viewModelScope.launch { companyRepo.all().collect { _companies.value = it } }
        viewModelScope.launch { venueRepo.allVenues().collect { _venueCount.value = it.size } }
        viewModelScope.launch {
            reservationRepo.all().collect { list ->
                _reservations.value = list.sortedByDescending { it.createdAt }
                _revenuePerCompany.value = Revenue.perCompany(list)
            }
        }
        refreshPayments()
    }

    fun refreshPayments() = viewModelScope.launch {
        val p = paymentRepo.all()
        _payments.value = p.sortedByDescending { it.createdAt }
        _totalRevenue.value = Revenue.total(p)
    }
}
