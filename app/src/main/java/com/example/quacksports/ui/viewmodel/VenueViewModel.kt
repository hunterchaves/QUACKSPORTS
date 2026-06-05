package com.example.quacksports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.VenueRepository
import com.example.quacksports.model.Venue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class VenueViewModel(private val repository: VenueRepository = VenueRepository()) : ViewModel() {

    private val _venues = MutableStateFlow<List<Venue>>(emptyList())
    val venues: StateFlow<List<Venue>> = _venues

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchVenues()
    }

    private fun fetchVenues() {
        viewModelScope.launch {
            repository.getVenues()
                .catch { exception ->
                    _errorMessage.value = "Error al cargar canchas: ${exception.message}"
                }
                .collect {
                    _venues.value = it
                    _errorMessage.value = null
                }
        }
    }
}
