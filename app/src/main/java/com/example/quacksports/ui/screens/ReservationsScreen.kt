package com.example.quacksports.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.* // Importa todos os componentes Material3
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.model.Reservation
import com.example.quacksports.ui.viewmodel.ReservationViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(
    onBack: () -> Unit,
    reservationViewModel: ReservationViewModel = viewModel()
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val reservations by reservationViewModel.userReservations.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let {
            reservationViewModel.fetchUserReservations(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Reservas") },
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
    ) { paddingValues ->
        if (currentUser == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Text("Faça login para ver suas reservas.", modifier = Modifier.padding(16.dp))
            }
        } else if (reservations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Text("Você não tem reservas ativas.", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            items(reservations, key = { it.id }) { reservation ->
                ReservationItem(reservation = reservation)
            }
            }
        }
    }
}

@Composable
fun ReservationItem(reservation: Reservation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = reservation.venueTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Data: ${reservation.date} ${reservation.time}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Preço: ${reservation.price}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Status: ${reservation.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
