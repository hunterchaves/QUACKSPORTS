package com.example.quacksports.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.model.Card
import com.example.quacksports.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(onBack: () -> Unit, vm: ProfileViewModel = viewModel()) {
    val cards by vm.cards.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Cartões") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Filled.Add, "Adicionar") }
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            items(cards, key = { it.id }) { card ->
                ListItem(
                    headlineContent = { Text("${card.brand} ****${card.last4}") },
                    supportingContent = { Text("${card.holderName} • %02d/%d".format(card.expMonth, card.expYear)) },
                    trailingContent = {
                        IconButton(onClick = { vm.deleteCard(card.id) }) { Icon(Icons.Filled.Delete, "Excluir") }
                    }
                )
                HorizontalDivider()
            }
        }
    }

    if (showDialog) {
        CardDialog(onDismiss = { showDialog = false }, onSave = { vm.addCard(it); showDialog = false })
    }
}

@Composable
private fun CardDialog(onDismiss: () -> Unit, onSave: (Card) -> Unit) {
    var holder by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("Visa") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo cartão") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(holder, { holder = it }, label = { Text("Nome no cartão") }, singleLine = true)
                OutlinedTextField(number, { input -> number = input.filter { it.isDigit() } }, label = { Text("Número do cartão") }, singleLine = true)
                OutlinedTextField(brand, { brand = it }, label = { Text("Bandeira") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(month, { input -> month = input.filter { it.isDigit() } }, label = { Text("Mês") }, singleLine = true, modifier = Modifier.weight(1f))
                    OutlinedTextField(year, { input -> year = input.filter { it.isDigit() } }, label = { Text("Ano") }, singleLine = true, modifier = Modifier.weight(1f))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(Card(
                    brand = brand.ifBlank { "Cartão" },
                    last4 = number.takeLast(4),
                    holderName = holder,
                    expMonth = month.toIntOrNull() ?: 0,
                    expYear = year.toIntOrNull() ?: 0
                ))
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
