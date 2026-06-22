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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.model.Venue
import com.example.quacksports.ui.viewmodel.CompanyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageVenuesScreen(
    companyId: String,
    onBack: () -> Unit,
    onEditCourts: (String) -> Unit,
    vm: CompanyViewModel = viewModel()
) {
    LaunchedEffect(companyId) { if (companyId.isNotBlank()) vm.bind(companyId) }
    val venues by vm.venues.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locais e Quadras") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = { FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Filled.Add, "Adicionar local") } }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            items(venues, key = { it.id }) { v ->
                ListItem(
                    headlineContent = { Text(v.name) },
                    supportingContent = { Text("${v.addressLine} - ${v.city}/${v.state}") },
                    trailingContent = { TextButton(onClick = { onEditCourts(v.id) }) { Text("Quadras") } }
                )
                HorizontalDivider()
            }
        }
    }

    if (showDialog) {
        VenueDialog(companyId = companyId, onDismiss = { showDialog = false }, onSave = { vm.saveVenue(it) {}; showDialog = false })
    }
}

@Composable
private fun VenueDialog(companyId: String, onDismiss: () -> Unit, onSave: (Venue) -> Unit) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }
    var lng by remember { mutableStateOf("") }
    var photo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo local") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(name, { name = it }, label = { Text("Nome do local") }, singleLine = true)
                OutlinedTextField(address, { address = it }, label = { Text("Endereço") }, singleLine = true)
                OutlinedTextField(city, { city = it }, label = { Text("Cidade") }, singleLine = true)
                OutlinedTextField(state, { state = it }, label = { Text("Estado") }, singleLine = true)
                OutlinedTextField(lat, { lat = it }, label = { Text("Latitude") }, singleLine = true)
                OutlinedTextField(lng, { lng = it }, label = { Text("Longitude") }, singleLine = true)
                OutlinedTextField(photo, { photo = it }, label = { Text("URL da foto") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(Venue(
                    companyId = companyId, name = name, addressLine = address, city = city, state = state,
                    lat = lat.toDoubleOrNull() ?: 0.0, lng = lng.toDoubleOrNull() ?: 0.0,
                    photos = if (photo.isBlank()) emptyList() else listOf(photo)
                ))
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
