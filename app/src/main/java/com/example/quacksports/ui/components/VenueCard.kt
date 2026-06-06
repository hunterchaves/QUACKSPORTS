package com.example.quacksports.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quacksports.model.Venue

@Composable
fun VenueCard(venue: Venue, priceFrom: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        AsyncImage(
            model = venue.imageUrl,
            contentDescription = venue.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(280.dp).clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(venue.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = "Rating", modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(venue.ratingAvg.toString(), fontSize = 14.sp)
            }
        }
        Spacer(Modifier.height(2.dp))
        Text("${venue.city}, ${venue.state}", color = Color.Gray, fontSize = 14.sp)
        if (priceFrom.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(priceFrom, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
