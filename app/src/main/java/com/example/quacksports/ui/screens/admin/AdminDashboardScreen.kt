package com.example.quacksports.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.ui.components.RevenueStatCard
import com.example.quacksports.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onBack: () -> Unit,
    vm: AdminViewModel = viewModel()
) {
    val companies by vm.companies.collectAsState()
    val reservations by vm.reservations.collectAsState()
    val payments by vm.payments.collectAsState()
    val venueCount by vm.venueCount.collectAsState()
    val totalRevenue by vm.totalRevenue.collectAsState()
    val revenuePerCompany by vm.revenuePerCompany.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Faturamento") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
                actions = { IconButton(onClick = { vm.refreshPayments() }) { Icon(Icons.Filled.Refresh, "Atualizar") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { RevenueStatCard("Faturamento total", "R$ %.2f".format(totalRevenue)) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RevenueStatCard("Empresas", companies.size.toString(), Modifier.weight(1f))
                    RevenueStatCard("Locais", venueCount.toString(), Modifier.weight(1f))
                }
            }
            item { RevenueStatCard("Reservas", reservations.size.toString()) }

            item { Text("Faturamento por empresa", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            items(companies, key = { it.id }) { c ->
                ListItem(
                    headlineContent = { Text(c.name) },
                    trailingContent = { Text("R$ %.2f".format(revenuePerCompany[c.id] ?: 0.0), fontWeight = FontWeight.Bold) }
                )
                HorizontalDivider()
            }

            item { Text("Pagamentos recentes", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            if (payments.isEmpty()) {
                item { Text("Nenhum pagamento ainda.", color = Color.Gray) }
            } else {
                items(payments, key = { it.id }) { p ->
                    ListItem(
                        headlineContent = { Text("R$ %.2f".format(p.amount)) },
                        supportingContent = { Text(p.status) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
