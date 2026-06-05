package com.example.quacksports.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.quacksports.ui.viewmodel.ReservationViewModel
import com.example.quacksports.ui.viewmodel.VenueViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DetailScreen(
    venueId: String?,
    onBack: () -> Unit,
    venueViewModel: VenueViewModel = viewModel(),
    reservationViewModel: ReservationViewModel = viewModel()
) {
    val venues by venueViewModel.venues.collectAsState()
    val venue = venues.find { it.id == venueId }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    // Initialize date/time once venue is loaded
    LaunchedEffect(venue) {
        if (venue != null && selectedDate.isEmpty()) {
            selectedDate = venue.date
            selectedTime = "10:00" // Default time
        }
    }

    LaunchedEffect(key1 = reservationViewModel.reservationSuccess) {
        if (reservationViewModel.reservationSuccess) {
            Toast.makeText(context, "Reserva realizada con sucesso!", Toast.LENGTH_LONG).show()
            reservationViewModel.reservationSuccess = false // Reset state
        }
    }

    LaunchedEffect(key1 = reservationViewModel.errorMessage) {
        reservationViewModel.errorMessage?.let { message ->
            Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
            reservationViewModel.errorMessage = null // Reset state
        }
    }

    if (venue == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFE51D53))
        }
        return
    }

    Scaffold(
        bottomBar = {
            Surface(
                shadowElevation = 16.dp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = venue.price, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = selectedDate, textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline, fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            if (auth.currentUser == null) {
                                Toast.makeText(context, "Por favor, inicie sesión para reservar.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            
                            reservationViewModel.createReservation(
                                venueId = venue.id,
                                venueTitle = venue.title,
                                date = selectedDate,
                                time = selectedTime,
                                price = venue.price,
                                onSuccess = { }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE51D53)),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                        enabled = !reservationViewModel.isLoading
                    ) {
                        if (reservationViewModel.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Reservar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Box {
                AsyncImage(
                    model = venue.imageUrl,
                    contentDescription = venue.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
            
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = venue.title, fontSize = 26.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = "Rating", modifier = Modifier.size(16.dp), tint = Color(0xFFFFB400))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${venue.rating} • ${venue.location}", fontWeight = FontWeight.Medium)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(text = "Fecha y Hora de la Reserva", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = { selectedDate = it },
                        label = { Text("Fecha") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = selectedTime,
                        onValueChange = { selectedTime = it },
                        label = { Text("Hora") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "O que este lugar oferece", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("• Vestiários\n• Iluminação LED\n• Estacionamento gratuito\n• Churrasqueira (opcional)", lineHeight = 24.sp)
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
