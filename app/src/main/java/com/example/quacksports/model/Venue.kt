package com.example.quacksports.model

data class Venue(
    val id: String = "",
    val title: String = "",
    val location: String = "",
    val distance: String = "",
    val date: String = "",
    val price: String = "",
    val rating: Double = 0.0,
    val imageUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

val sampleVenues = listOf(
    Venue(
        id = "1",
        title = "Arena Society dos Patos",
        location = "São Paulo, SP",
        distance = "2 km de distância",
        date = "15-20 de Mar",
        price = "R$ 150 / hora",
        rating = 4.9,
        imageUrl = "https://images.unsplash.com/photo-1579952363873-27f3bade9f55?q=80&w=1000&auto=format&fit=crop",
        latitude = -23.5505,
        longitude = -46.6333
    ),
    Venue(
        id = "2",
        title = "Quadra Poliesportiva Central",
        location = "Campinas, SP",
        distance = "85 km de distância",
        date = "22-25 de Mar",
        price = "R$ 80 / hora",
        rating = 4.7,
        imageUrl = "https://images.unsplash.com/photo-1546519638-68e109498ffc?q=80&w=1000&auto=format&fit=crop",
        latitude = -22.9064,
        longitude = -47.0616
    ),
    Venue(
        id = "3",
        title = "Clube de Tênis Ace",
        location = "Rio de Janeiro, RJ",
        distance = "400 km de distância",
        date = "10-12 de Abr",
        price = "R$ 200 / hora",
        rating = 5.0,
        imageUrl = "https://images.unsplash.com/photo-1595435934249-5df7ed86e1c0?q=80&w=1000&auto=format&fit=crop",
        latitude = -22.9068,
        longitude = -43.1729
    ),
    Venue(
        id = "4",
        title = "Ginásio de Vôlei Praia",
        location = "Santos, SP",
        distance = "70 km de distância",
        date = "05-08 de Mai",
        price = "R$ 120 / hora",
        rating = 4.8,
        imageUrl = "https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?q=80&w=1000&auto=format&fit=crop",
        latitude = -23.9608,
        longitude = -46.3339
    )
)