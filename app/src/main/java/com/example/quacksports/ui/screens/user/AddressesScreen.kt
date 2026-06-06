package com.example.quacksports.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.model.Address
import com.example.quacksports.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressesScreen(onBack: () -> Unit, vm: ProfileViewModel = viewModel()) {
    val addresses by vm.addresses.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Endereços") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Filled.Add, "Adicionar") }
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            items(addresses, key = { it.id }) { addr ->
                ListItem(
                    headlineContent = { Text(addr.label.ifBlank { addr.street }) },
                    supportingContent = { Text("${addr.street}, ${addr.number} - ${addr.city}/${addr.state}") },
                    trailingContent = {
                        IconButton(onClick = { vm.deleteAddress(addr.id) }) { Icon(Icons.Filled.Delete, "Excluir") }
                    }
                )
                HorizontalDivider()
            }
        }
    }

    if (showDialog) {
        AddressDialog(onDismiss = { showDialog = false }, onSave = { vm.addAddress(it); showDialog = false })
    }
}

@Composable
private fun AddressDialog(onDismiss: () -> Unit, onSave: (Address) -> Unit) {
    var label by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var complement by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var zip by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo endereço") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(label, { label = it }, label = { Text("Identificação (casa, trabalho)") }, singleLine = true)
                OutlinedTextField(street, { street = it }, label = { Text("Rua") }, singleLine = true)
                OutlinedTextField(number, { number = it }, label = { Text("Número") }, singleLine = true)
                OutlinedTextField(complement, { complement = it }, label = { Text("Complemento") }, singleLine = true)
                OutlinedTextField(city, { city = it }, label = { Text("Cidade") }, singleLine = true)
                OutlinedTextField(state, { state = it }, label = { Text("Estado") }, singleLine = true)
                OutlinedTextField(zip, { zip = it }, label = { Text("CEP") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(Address(label = label, street = street, number = number, complement = complement, city = city, state = state, zip = zip))
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
