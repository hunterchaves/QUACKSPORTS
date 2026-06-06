package com.example.quacksports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.logic.Revenue
import com.example.quacksports.data.repository.ReservationRepository
import com.example.quacksports.data.repository.VenueRepository
import com.example.quacksports.model.Court
import com.example.quacksports.model.Reservation
import com.example.quacksports.model.Venue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompanyViewModel(
    private val venueRepo: VenueRepository = VenueRepository(),
    private val reservationRepo: ReservationRepository = ReservationRepository()
) : ViewModel() {
    private val _venues = MutableStateFlow<List<Venue>>(emptyList())
    val venues: StateFlow<List<Venue>> = _venues
    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    val reservations: StateFlow<List<Reservation>> = _reservations
    private val _revenue = MutableStateFlow(0.0)
    val revenue: StateFlow<Double> = _revenue

    fun bind(companyId: String) {
        viewModelScope.launch { venueRepo.venuesByCompany(companyId).collect { _venues.value = it } }
        viewModelScope.launch {
            reservationRepo.byCompany(companyId).collect { list ->
                _reservations.value = list.sortedByDescending { it.createdAt }
                _revenue.value = Revenue.perCompany(list)[companyId] ?: 0.0
            }
        }
    }

    fun saveVenue(venue: Venue, onDone: () -> Unit) =
        viewModelScope.launch { venueRepo.upsertVenue(venue); onDone() }

    fun saveCourt(venueId: String, court: Court, onDone: () -> Unit) =
        viewModelScope.launch { venueRepo.upsertCourt(venueId, court); onDone() }
}
