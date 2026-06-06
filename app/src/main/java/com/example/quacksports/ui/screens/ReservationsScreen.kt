package com.example.quacksports.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
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
        currentUser?.uid?.let { reservationViewModel.load(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Reservas") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        }
    ) { paddingValues ->
        if (currentUser == null) {
            Box(Modifier.fillMaxSize().padding(paddingValues)) {
                Text("Faça login para ver suas reservas.", modifier = Modifier.padding(16.dp))
            }
        } else if (reservations.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(paddingValues)) {
                Text("Você não tem reservas ativas.", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(paddingValues)) {
                items(reservations, key = { it.id }) { ReservationItem(it) }
            }
        }
    }
}

@Composable
fun ReservationItem(reservation: Reservation) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("${reservation.venueName} — ${reservation.courtName}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Data: ${reservation.date}  %02d:00–%02d:00".format(reservation.startHour, reservation.endHour), style = MaterialTheme.typography.bodyMedium)
            Text("Valor: R$ %.2f".format(reservation.amount), style = MaterialTheme.typography.bodyMedium)
            Text("Status: ${reservation.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
