package com.example.quacksports.ui.screens.user

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.model.Reservation
import com.example.quacksports.ui.components.CardPickerRow
import com.example.quacksports.ui.viewmodel.PaymentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    draft: Reservation,
    onBack: () -> Unit,
    onPaid: () -> Unit,
    onAddCard: () -> Unit,
    vm: PaymentViewModel = viewModel()
) {
    val context = LocalContext.current
    val cards by vm.cards.collectAsState()

    LaunchedEffect(vm.success) {
        if (vm.success) {
            Toast.makeText(context, "Pagamento aprovado! Reserva confirmada.", Toast.LENGTH_LONG).show()
            onPaid()
        }
    }
    LaunchedEffect(vm.errorMessage) {
        vm.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            vm.errorMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagamento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            Card(elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("${draft.venueName} — ${draft.courtName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Data: ${draft.date}  %02d:00–%02d:00".format(draft.startHour, draft.endHour))
                    Text("Valor: R$ %.2f".format(draft.amount), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Forma de pagamento", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            CardPickerRow(cards = cards, selectedId = vm.selectedCardId, onSelect = { vm.selectedCardId = it }, onAddCard = onAddCard)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { vm.payAndReserve(draft) { } },
                enabled = !vm.isProcessing,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE51D53)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                if (vm.isProcessing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Pagar R$ %.2f".format(draft.amount), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
