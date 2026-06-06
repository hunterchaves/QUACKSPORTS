package com.example.quacksports.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.logic.Availability
import com.example.quacksports.data.repository.ReservationRepository
import com.example.quacksports.data.repository.VenueRepository
import com.example.quacksports.model.Court
import com.example.quacksports.model.TimeSlot
import com.example.quacksports.model.Venue
import kotlinx.coroutines.launch

class BookingViewModel(
    private val venueRepo: VenueRepository = VenueRepository(),
    private val reservationRepo: ReservationRepository = ReservationRepository()
) : ViewModel() {
    var venue by mutableStateOf<Venue?>(null); private set
    var court by mutableStateOf<Court?>(null); private set
    var selectedDate by mutableStateOf("")
    var slots by mutableStateOf<List<TimeSlot>>(emptyList()); private set
    var selectedHour by mutableStateOf<Int?>(null)
    var isLoading by mutableStateOf(false); private set

    fun load(venueId: String, courtId: String) {
        viewModelScope.launch {
            isLoading = true
            venue = venueRepo.getVenue(venueId)
            court = venueRepo.getCourt(venueId, courtId)
            isLoading = false
        }
    }

    fun pickDate(date: String) {
        selectedDate = date
        selectedHour = null
        val c = court ?: return
        viewModelScope.launch {
            val taken = reservationRepo.forCourtOnDate(c.id, date)
            slots = Availability.slotsFor(c.openHour, c.closeHour, taken)
        }
    }
}
