package com.example.quacksports.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.quacksports.model.UserRole
import com.example.quacksports.ui.viewmodel.AuthViewModel
import com.example.quacksports.ui.viewmodel.ProfileViewModel
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onNavigateToReservations: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToCards: () -> Unit,
    onNavigateToCompany: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToSeed: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val user = authViewModel.currentUserData
    val photoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { profileViewModel.setAvatar(it) { authViewModel.refresh() } }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 24.dp, top = 32.dp)
                .combinedClickable(onClick = {}, onLongClick = onNavigateToSeed)
        )

        val photoPath = user?.photoPath ?: ""
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFEEEEEE))
                .combinedClickable(onClick = { photoLauncher.launch("image/*") }, onLongClick = onNavigateToSeed),
            contentAlignment = Alignment.Center
        ) {
            if (photoPath.isNotBlank()) {
                AsyncImage(
                    model = File(photoPath),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
            }
        }
        Spacer(Modifier.height(16.dp))

        if (user != null) {
            Text("${user.firstName} ${user.lastName}", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text(user.email, fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))
        } else {
            Text("Carregando perfil...", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))
        }

        OutlinedButton(onClick = onNavigateToAddresses, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text("Meus Endereços")
        }
        OutlinedButton(onClick = onNavigateToCards, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text("Meus Cartões")
        }
        OutlinedButton(onClick = onNavigateToReservations, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text("Minhas Reservas")
        }
        if (authViewModel.role == UserRole.COMPANY) {
            OutlinedButton(onClick = onNavigateToCompany, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text("Painel da Empresa")
            }
        }
        if (authViewModel.role == UserRole.ADMIN) {
            OutlinedButton(onClick = onNavigateToAdmin, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text("Faturamento (Admin)")
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE51D53))
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text("Sair")
        }
    }
}
