package com.example.quacksports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quacksports.data.repository.VenueRepository
import com.example.quacksports.model.Court
import com.example.quacksports.model.Sport
import com.example.quacksports.model.Venue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class VenueViewModel(private val repository: VenueRepository = VenueRepository()) : ViewModel() {
    private val _allVenues = MutableStateFlow<List<Venue>>(emptyList())
    private val _allCourts = MutableStateFlow<List<Court>>(emptyList())
    private val _selectedSport = MutableStateFlow<Sport?>(null)
    val selectedSport: StateFlow<Sport?> = _selectedSport

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _venues = MutableStateFlow<List<Venue>>(emptyList())
    val venues: StateFlow<List<Venue>> = _venues

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        _isLoading.value = true
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
        viewModelScope.launch {
            repository.allCourts()
                .catch {
                    // Silently handle error for courts loading
                }
                .collect {
                    _allCourts.value = it
                    applyFilter()
                }
        }
    }

    fun toggleSport(sport: Sport) {
        _selectedSport.value = if (_selectedSport.value == sport) null else sport
        applyFilter()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilter()
    }

    private fun applyFilter() {
        val selected = _selectedSport.value
        val query = _searchQuery.value.trim()
        val allCourtsList = _allCourts.value

        var filtered = _allVenues.value

        // 1. Filtrar por esporte selecionado se houver
        if (selected != null) {
            filtered = filtered.filter { venue ->
                allCourtsList.any { court ->
                    court.venueId == venue.id && court.sport == selected.name
                }
            }
        }

        // 2. Filtrar por termo de pesquisa se houver
        if (query.isNotEmpty()) {
            filtered = filtered.filter { venue ->
                val matchesVenueInfo = venue.name.contains(query, ignoreCase = true) ||
                        venue.city.contains(query, ignoreCase = true) ||
                        venue.state.contains(query, ignoreCase = true) ||
                        venue.addressLine.contains(query, ignoreCase = true)

                val matchesCourtSport = allCourtsList.any { court ->
                    if (court.venueId == venue.id) {
                        val s = Sport.from(court.sport)
                        s.label.contains(query, ignoreCase = true) || s.name.contains(query, ignoreCase = true)
                    } else {
                        false
                    }
                }

                matchesVenueInfo || matchesCourtSport
            }
        }

        _venues.value = filtered
    }
}
