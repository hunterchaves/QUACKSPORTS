package com.example.quacksports.ui.screens.user

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quacksports.data.repository.VenueRepository
import com.example.quacksports.model.Venue
import com.example.quacksports.ui.components.CourtCard

@Composable
fun VenueDetailScreen(
    venueId: String,
    onBack: () -> Unit,
    onCourtSelected: (String, String) -> Unit
) {
    val repo = remember { VenueRepository() }
    val venue by produceState<Venue?>(null, venueId) { value = repo.getVenue(venueId) }
    val courts by repo.courts(venueId).collectAsState(initial = emptyList())

    val v = venue
    if (v == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFE51D53))
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box {
            AsyncImage(
                model = v.imageUrl,
                contentDescription = v.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(16.dp).background(Color.White, CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        }
        Column(Modifier.padding(24.dp)) {
            Text(v.name, fontSize = 26.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFFFB400))
                Spacer(Modifier.width(4.dp))
                Text("${v.ratingAvg} • ${v.city}, ${v.state}", fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.height(8.dp))
            Text(v.addressLine, color = Color.Gray, fontSize = 14.sp)
            Spacer(Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(Modifier.height(24.dp))
            Text("Quadras disponíveis", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            if (courts.isEmpty()) {
                Text("Nenhuma quadra cadastrada.", color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
            } else {
                courts.forEach { court ->
                    CourtCard(court = court, onClick = { onCourtSelected(venueId, court.id) })
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
