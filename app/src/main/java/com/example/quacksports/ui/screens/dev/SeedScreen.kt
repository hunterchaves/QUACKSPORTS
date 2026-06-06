package com.example.quacksports.ui.screens.dev

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.data.repository.CompanyRepository
import com.example.quacksports.data.repository.DataSeeder
import com.example.quacksports.model.Company
import com.example.quacksports.model.UserRole
import com.example.quacksports.ui.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeedScreen(onBack: () -> Unit, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val seeder = remember { DataSeeder() }
    val companyRepo = remember { CompanyRepository() }

    var companies by remember { mutableStateOf<List<Company>>(emptyList()) }
    var selectedCompany by remember { mutableStateOf<Company?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var seeding by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { companyRepo.all().collect { companies = it } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seed / Dev") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Papel atual: ${authViewModel.role.name}", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Button(
                onClick = {
                    seeding = true
                    scope.launch {
                        val n = try { seeder.seed() } catch (e: Exception) { -1 }
                        seeding = false
                        val msg = when {
                            n > 0 -> "$n empresas cadastradas"
                            n == 0 -> "Dados já existem"
                            else -> "Erro ao popular dados"
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                },
                enabled = !seeding,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (seeding) "Populando..." else "Popular dados (5 empresas)") }

            HorizontalDivider()
            Text("Definir meu papel", fontWeight = FontWeight.Bold)

            Button(
                onClick = { authViewModel.setRole(UserRole.USER, "") { Toast.makeText(context, "Agora: USUÁRIO", Toast.LENGTH_SHORT).show() } },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Virar USUÁRIO") }

            Button(
                onClick = { authViewModel.setRole(UserRole.ADMIN, "") { Toast.makeText(context, "Agora: ADMIN", Toast.LENGTH_SHORT).show() } },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Virar ADMIN") }

            Text("Empresa para gerenciar (papel EMPRESA):")
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedCompany?.name ?: "Selecione uma empresa",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    companies.forEach { c ->
                        DropdownMenuItem(text = { Text(c.name) }, onClick = { selectedCompany = c; expanded = false })
                    }
                }
            }
            Button(
                onClick = {
                    val c = selectedCompany ?: return@Button
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                    authViewModel.setRole(UserRole.COMPANY, c.id) {
                        scope.launch { companyRepo.setOwner(c.id, uid) }
                        Toast.makeText(context, "Agora: EMPRESA (${c.name})", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = selectedCompany != null,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Virar EMPRESA") }
        }
    }
}
