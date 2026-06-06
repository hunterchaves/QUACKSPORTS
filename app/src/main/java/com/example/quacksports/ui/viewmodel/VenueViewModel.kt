package com.example.quacksports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.repository.VenueRepository
import com.example.quacksports.model.Sport
import com.example.quacksports.model.Venue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class VenueViewModel(private val repository: VenueRepository = VenueRepository()) : ViewModel() {
    private val _allVenues = MutableStateFlow<List<Venue>>(emptyList())
    private val _selectedSport = MutableStateFlow<Sport?>(null)
    val selectedSport: StateFlow<Sport?> = _selectedSport

    private val _venues = MutableStateFlow<List<Venue>>(emptyList())
    val venues: StateFlow<List<Venue>> = _venues

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            repository.allVenues()
                .catch {
                    _isLoading.value = false
                    _errorMessage.value = "Erro ao carregar quadras: ${it.message}"
                }
                .collect {
                    _allVenues.value = it
                    applyFilter()
                    _errorMessage.value = null
                    _isLoading.value = false
                }
        }
    }

    fun toggleSport(sport: Sport) {
        _selectedSport.value = if (_selectedSport.value == sport) null else sport
        applyFilter()
    }

    private fun applyFilter() {
        _venues.value = _allVenues.value
    }
}
