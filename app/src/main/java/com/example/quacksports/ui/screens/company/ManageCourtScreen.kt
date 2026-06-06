package com.example.quacksports.ui.screens.company

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.data.repository.VenueRepository
import com.example.quacksports.model.Court
import com.example.quacksports.model.Sport
import com.example.quacksports.ui.viewmodel.CompanyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCourtScreen(
    venueId: String,
    onBack: () -> Unit,
    vm: CompanyViewModel = viewModel()
) {
    val repo = remember { VenueRepository() }
    val courts by repo.courts(venueId).collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quadras do local") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = { FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Filled.Add, "Adicionar quadra") } }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            items(courts, key = { it.id }) { c ->
                ListItem(
                    headlineContent = { Text(c.name) },
                    supportingContent = { Text("${Sport.from(c.sport).label} • R$ %.0f/h • %02d:00-%02d:00".format(c.pricePerHour, c.openHour, c.closeHour)) }
                )
                HorizontalDivider()
            }
        }
    }

    if (showDialog) {
        CourtDialog(venueId = venueId, onDismiss = { showDialog = false }, onSave = { vm.saveCourt(venueId, it) {}; showDialog = false })
    }
}

@Composable
private fun CourtDialog(venueId: String, onDismiss: () -> Unit, onSave: (Court) -> Unit) {
    var name by remember { mutableStateOf("") }
    var sport by remember { mutableStateOf(Sport.SOCCER) }
    var price by remember { mutableStateOf("") }
    var open by remember { mutableStateOf("8") }
    var close by remember { mutableStateOf("22") }
    var photo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova quadra") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(name, { name = it }, label = { Text("Nome (ex: Quadra 1)") }, singleLine = true)
                Text("Esporte")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Sport.entries.forEach { s ->
                        FilterChip(selected = sport == s, onClick = { sport = s }, label = { Text(s.label) })
                    }
                }
                OutlinedTextField(price, { input -> price = input.filter { it.isDigit() || it == '.' } }, label = { Text("Preço por hora") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(open, { input -> open = input.filter { it.isDigit() } }, label = { Text("Abre (h)") }, singleLine = true, modifier = Modifier.weight(1f))
                    OutlinedTextField(close, { input -> close = input.filter { it.isDigit() } }, label = { Text("Fecha (h)") }, singleLine = true, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(photo, { photo = it }, label = { Text("URL da foto") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(Court(
                    venueId = venueId, name = name, sport = sport.name,
                    pricePerHour = price.toDoubleOrNull() ?: 0.0,
                    photos = if (photo.isBlank()) emptyList() else listOf(photo),
                    openHour = open.toIntOrNull() ?: 8, closeHour = close.toIntOrNull() ?: 22
                ))
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
