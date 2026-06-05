package com.example.quacksports.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quacksports.model.Venue
// import com.example.quacksports.model.sampleVenues // Remove this import
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quacksports.ui.viewmodel.VenueViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToMap: () -> Unit,
    viewModel: VenueViewModel = viewModel()
) {
    val venues by viewModel.venues.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar()
            CategoryRow()
            
            if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else if (venues.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFE51D53))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(venues) { venue ->
                        VenueCard(venue = venue, onClick = { onNavigateToDetail(venue.id) })
                    }
                }
            }
        }
        
        // Floating Map Button
        ExtendedFloatingActionButton(
            onClick = onNavigateToMap,
            icon = { Icon(Icons.Filled.Map, "Mapa") },
            text = { Text("Mapa") },
            containerColor = Color.Black,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
fun SearchBar() {
    Surface(
        shape = RoundedCornerShape(32.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.Black)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Para onde?", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Text("Qualquer esporte • Qualquer data", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

data class Category(val name: String, val icon: ImageVector)

@Composable
fun CategoryRow() {
    val categories = listOf(
        Category("Futebol", Icons.Filled.SportsSoccer),
        Category("Tênis", Icons.Filled.SportsTennis),
        Category("Basquete", Icons.Filled.SportsBasketball),
        Category("Vôlei", Icons.Filled.SportsVolleyball)
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        items(categories) { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { }
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.name,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun VenueCard(venue: Venue, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = venue.imageUrl,
            contentDescription = venue.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = venue.location, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = "Rating", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = venue.rating.toString(), fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = venue.distance, color = Color.Gray, fontSize = 14.sp)
        Text(text = venue.date, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = venue.price, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}