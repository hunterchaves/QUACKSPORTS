package com.example.quacksports.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.ui.viewmodel.VenueViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: VenueViewModel = viewModel()
) {
    val venues by viewModel.venues.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val saopaulo = LatLng(-23.5505, -46.6333)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(saopaulo, 10f)
    }

    LaunchedEffect(venues) {
        venues.firstOrNull()?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(it.lat, it.lng), 11f)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mapa de Quadras") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = false)
            ) {
                venues.forEach { venue ->
                    Marker(
                        state = MarkerState(position = LatLng(venue.lat, venue.lng)),
                        title = venue.name,
                        snippet = "${venue.city}, ${venue.state}",
                        onInfoWindowClick = { onNavigateToDetail(venue.id) }
                    )
                }
            }

            if (isLoading) {
                LinearProgressIndicator(
                    color = Color(0xFFE51D53),
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
                )
            } else if (venues.isEmpty()) {
                Surface(
                    color = Color.White,
                    tonalElevation = 3.dp,
                    modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)
                ) {
                    Text(
                        text = "Nenhum ponto cadastrado.",
                        color = Color.Gray,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}
