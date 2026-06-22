package com.example.quacksports.ui.screens.admin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    onLogout: () -> Unit,
    vm: AdminViewModel = viewModel()
) {
    val companies by vm.companies.collectAsState()
    val reservations by vm.reservations.collectAsState()
    val payments by vm.payments.collectAsState()
    val venueCount by vm.venueCount.collectAsState()
    val totalRevenue by vm.totalRevenue.collectAsState()
    val revenuePerCompany by vm.revenuePerCompany.collectAsState()
    val isCreatingCompany by vm.isCreatingCompany.collectAsState()
    val companyMessage by vm.companyMessage.collectAsState()
    val context = LocalContext.current
    var showCompanyDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    LaunchedEffect(companyMessage) {
        companyMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            vm.clearCompanyMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Faturamento") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
                actions = {
                    IconButton(onClick = { vm.refreshPayments() }) {
                        Icon(Icons.Filled.Refresh, "Atualizar")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Sair")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedButton(
                    onClick = { showCompanyDialog = true },
                    enabled = !isCreatingCompany,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isCreatingCompany) "Cadastrando empresa..." else "Cadastrar empresa")
                }
            }
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
                    supportingContent = {
                        if (c.email.isNotBlank()) Text("Login: ${c.email}")
                    },
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

    if (showCompanyDialog) {
        val cnpjData by vm.cnpjData.collectAsState()
        val isSearchingCnpj by vm.isSearchingCnpj.collectAsState()

        CreateCompanyDialog(
            isSaving = isCreatingCompany,
            isSearchingCnpj = isSearchingCnpj,
            cnpjData = cnpjData,
            onDismiss = {
                if (!isCreatingCompany) {
                    showCompanyDialog = false
                    vm.clearCnpjData()
                }
            },
            onSearchCnpj = { vm.searchCnpj(it) },
            onSave = { name, email, logoUrl, description ->
                vm.createCompany(context, name, email, logoUrl, description)
                showCompanyDialog = false
                vm.clearCnpjData()
            }
        )
    }
}

@Composable
private fun CreateCompanyDialog(
    isSaving: Boolean,
    isSearchingCnpj: Boolean,
    cnpjData: Map<String, String>?,
    onDismiss: () -> Unit,
    onSearchCnpj: (String) -> Unit,
    onSave: (name: String, email: String, logoUrl: String, description: String) -> Unit
) {
    var cnpj by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var name by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var email by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var logoUrl by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var description by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }

    androidx.compose.runtime.LaunchedEffect(cnpjData) {
        cnpjData?.let {
            name = it["name"] ?: name
            description = it["description"] ?: description
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cadastrar empresa") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        cnpj, { cnpj = it },
                        label = { Text("CNPJ") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                    )
                    Button(
                        onClick = { onSearchCnpj(cnpj) },
                        enabled = !isSearchingCnpj && cnpj.isNotBlank(),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        if (isSearchingCnpj) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Buscar")
                        }
                    }
                }
                OutlinedTextField(name, { name = it }, label = { Text("Nome da empresa") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(email, { email = it }, label = { Text("Email de acesso") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(logoUrl, { logoUrl = it }, label = { Text("URL do logo/foto") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(description, { description = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
                Text("Senha padrão: 123456", color = Color.Gray, fontSize = 12.sp)
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, email, logoUrl, description) },
                enabled = !isSaving && name.isNotBlank() && email.isNotBlank()
            ) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss, enabled = !isSaving) { Text("Cancelar") } }
    )
}
