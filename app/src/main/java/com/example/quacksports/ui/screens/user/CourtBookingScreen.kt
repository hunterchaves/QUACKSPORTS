package com.example.quacksports.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.model.Reservation
import com.example.quacksports.ui.components.SlotPickerGrid
import com.example.quacksports.ui.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtBookingScreen(
    venueId: String,
    courtId: String,
    onBack: () -> Unit,
    onProceedToPayment: (Reservation) -> Unit,
    vm: BookingViewModel = viewModel()
) {
    LaunchedEffect(venueId, courtId) { vm.load(venueId, courtId) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val court = vm.court
    val venue = vm.venue

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservar quadra") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        },
        bottomBar = {
            Surface(shadowElevation = 16.dp, color = Color.White) {
                Button(
                    onClick = {
                        val c = court ?: return@Button
                        val v = venue ?: return@Button
                        val h = vm.selectedHour ?: return@Button
                        onProceedToPayment(
                            Reservation(
                                companyId = v.companyId, venueId = v.id, courtId = c.id,
                                courtName = c.name, venueName = v.name, sport = c.sport,
                                date = vm.selectedDate, startHour = h, endHour = h + 1,
                                amount = c.pricePerHour
                            )
                        )
                    },
                    enabled = vm.selectedHour != null && court != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE51D53)),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    val priceText = court?.let { " (R$ %.0f)".format(it.pricePerHour) } ?: ""
                    Text("Reservar$priceText", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(24.dp)
        ) {
            if (court == null) {
                CircularProgressIndicator(color = Color(0xFFE51D53))
            } else {
                Text(court.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(venue?.name ?: "", color = Color.Gray)
                Spacer(Modifier.height(24.dp))
                Text("Escolha a data", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { showDatePicker = true }) {
                    Text(if (vm.selectedDate.isBlank()) "Selecionar data" else vm.selectedDate)
                }
                Spacer(Modifier.height(24.dp))
                if (vm.selectedDate.isNotBlank()) {
                    Text("Horários disponíveis", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    if (vm.slots.isEmpty()) {
                        Text("Carregando horários...", color = Color.Gray)
                    } else {
                        SlotPickerGrid(slots = vm.slots, selectedHour = vm.selectedHour, onSelect = { vm.selectedHour = it })
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        fmt.timeZone = TimeZone.getTimeZone("UTC")
                        vm.pickDate(fmt.format(Date(millis)))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
