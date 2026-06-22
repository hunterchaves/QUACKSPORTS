package com.example.quacksports.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quacksports.model.Court
import com.example.quacksports.model.Sport

private fun sportIcon(sport: String): ImageVector = when (Sport.from(sport)) {
    Sport.SOCCER -> Icons.Filled.SportsSoccer
    Sport.BASKETBALL -> Icons.Filled.SportsBasketball
    Sport.VOLLEYBALL -> Icons.Filled.SportsVolleyball
    Sport.MULTISPORT -> Icons.Filled.SportsHandball
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtCard(court: Court, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(sportIcon(court.sport), contentDescription = null, modifier = Modifier.size(36.dp), tint = Color(0xFFE51D53))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(court.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(Sport.from(court.sport).label, color = Color.Gray, fontSize = 13.sp)
                Text("R$ %.0f / hora".format(court.pricePerHour), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}
