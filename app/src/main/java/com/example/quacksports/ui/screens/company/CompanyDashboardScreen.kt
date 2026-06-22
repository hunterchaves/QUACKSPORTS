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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.ui.components.RevenueStatCard
import com.example.quacksports.ui.viewmodel.CompanyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDashboardScreen(
    companyId: String,
    onBack: () -> Unit,
    onManageVenues: () -> Unit,
    onViewReservations: () -> Unit,
    vm: CompanyViewModel = viewModel()
) {
    LaunchedEffect(companyId) { if (companyId.isNotBlank()) vm.bind(companyId) }
    val venues by vm.venues.collectAsState()
    val revenue by vm.revenue.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Painel da Empresa") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        }
    ) { padding ->
        if (companyId.isBlank()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Nenhuma empresa associada. Use a tela de Seed para vincular uma empresa.", modifier = Modifier.padding(24.dp))
            }
            return@Scaffold
        }
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RevenueStatCard("Faturamento", "R$ %.2f".format(revenue))
            RevenueStatCard("Locais cadastrados", venues.size.toString())
            Button(
                onClick = onManageVenues,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE51D53))
            ) { Text("Gerenciar locais e quadras") }
            OutlinedButton(onClick = onViewReservations, modifier = Modifier.fillMaxWidth()) { Text("Ver reservas") }
            Text("Meus locais", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(venues, key = { it.id }) { v ->
                    ListItem(
                        headlineContent = { Text(v.name) },
                        supportingContent = { Text("${v.city}/${v.state}") }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
