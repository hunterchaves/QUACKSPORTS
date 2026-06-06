package com.example.quacksports.ui.screens.company

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
import com.example.quacksports.ui.viewmodel.CompanyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyReservationsScreen(
    companyId: String,
    onBack: () -> Unit,
    vm: CompanyViewModel = viewModel()
) {
    LaunchedEffect(companyId) { if (companyId.isNotBlank()) vm.bind(companyId) }
    val reservations by vm.reservations.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas recebidas") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        }
    ) { padding ->
        if (reservations.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding)) { Text("Nenhuma reserva ainda.", modifier = Modifier.padding(16.dp)) }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(reservations, key = { it.id }) { r ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("${r.venueName} — ${r.courtName}", fontWeight = FontWeight.Bold)
                            Text("Data: ${r.date}  %02d:00–%02d:00".format(r.startHour, r.endHour))
                            Text("Valor: R$ %.2f".format(r.amount))
                            Text("Status: ${r.status}", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
