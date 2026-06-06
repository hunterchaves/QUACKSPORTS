package com.example.quacksports.data.logic

import com.example.quacksports.model.Sport

object SeedData {
    data class SeedCourt(val name: String, val sport: Sport, val pricePerHour: Double,
                         val photos: List<String>, val openHour: Int = 8, val closeHour: Int = 22)
    data class SeedVenue(val name: String, val addressLine: String, val city: String, val state: String,
                         val lat: Double, val lng: Double, val photos: List<String>,
                         val ratingAvg: Double, val courts: List<SeedCourt>)
    data class SeedCompany(val name: String, val description: String, val logoUrl: String,
                           val venues: List<SeedVenue>)

    private const val P_SOCCER = "https://images.unsplash.com/photo-1579952363873-27f3bade9f55?q=80&w=1000&auto=format&fit=crop"
    private const val P_BASKET = "https://images.unsplash.com/photo-1546519638-68e109498ffc?q=80&w=1000&auto=format&fit=crop"
    private const val P_TENNIS = "https://images.unsplash.com/photo-1595435934249-5df7ed86e1c0?q=80&w=1000&auto=format&fit=crop"
    private const val P_VOLLEY = "https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?q=80&w=1000&auto=format&fit=crop"
    private const val P_MULTI  = "https://images.unsplash.com/photo-1571902943202-507ec2618e8f?q=80&w=1000&auto=format&fit=crop"

    fun build(): List<SeedCompany> = listOf(
        SeedCompany("Arena Patos SP", "Complexo esportivo na zona oeste de São Paulo.", P_SOCCER,
            listOf(
                SeedVenue("Arena Patos - Pinheiros", "Rua dos Pinheiros, 1200", "São Paulo", "SP",
                    -23.5645, -46.6916, listOf(P_SOCCER, P_MULTI), 4.9, listOf(
                        SeedCourt("Quadra 1 - Society", Sport.SOCCER, 150.0, listOf(P_SOCCER)),
                        SeedCourt("Quadra 2 - Society", Sport.SOCCER, 140.0, listOf(P_SOCCER)),
                        SeedCourt("Quadra 3 - Poliesportiva", Sport.MULTISPORT, 90.0, listOf(P_MULTI))
                    )),
                SeedVenue("Arena Patos - Lapa", "Rua Clélia, 540", "São Paulo", "SP",
                    -23.5280, -46.7050, listOf(P_BASKET), 4.7, listOf(
                        SeedCourt("Quadra 1 - Basquete", Sport.BASKETBALL, 100.0, listOf(P_BASKET)),
                        SeedCourt("Quadra 2 - Vôlei", Sport.VOLLEYBALL, 110.0, listOf(P_VOLLEY))
                    ))
            )),
        SeedCompany("Campinas Sports Club", "Quadras cobertas em Campinas.", P_BASKET,
            listOf(
                SeedVenue("CSC - Cambuí", "Av. Júlio de Mesquita, 300", "Campinas", "SP",
                    -22.8970, -47.0530, listOf(P_BASKET, P_VOLLEY), 4.6, listOf(
                        SeedCourt("Quadra 1 - Basquete", Sport.BASKETBALL, 80.0, listOf(P_BASKET)),
                        SeedCourt("Quadra 2 - Vôlei", Sport.VOLLEYBALL, 85.0, listOf(P_VOLLEY)),
                        SeedCourt("Quadra 3 - Poliesportiva", Sport.MULTISPORT, 70.0, listOf(P_MULTI))
                    ))
            )),
        SeedCompany("Santos Beach Arena", "Vôlei e futebol perto da praia.", P_VOLLEY,
            listOf(
                SeedVenue("Santos Beach - Gonzaga", "Av. Ana Costa, 50", "Santos", "SP",
                    -23.9670, -46.3330, listOf(P_VOLLEY, P_SOCCER), 4.8, listOf(
                        SeedCourt("Quadra 1 - Vôlei", Sport.VOLLEYBALL, 120.0, listOf(P_VOLLEY)),
                        SeedCourt("Quadra 2 - Society", Sport.SOCCER, 130.0, listOf(P_SOCCER))
                    ))
            )),
        SeedCompany("Rio Quadras", "Rede de quadras no Rio de Janeiro.", P_SOCCER,
            listOf(
                SeedVenue("Rio Quadras - Copacabana", "Rua Barata Ribeiro, 200", "Rio de Janeiro", "RJ",
                    -22.9700, -43.1850, listOf(P_SOCCER, P_BASKET), 5.0, listOf(
                        SeedCourt("Quadra 1 - Society", Sport.SOCCER, 200.0, listOf(P_SOCCER)),
                        SeedCourt("Quadra 2 - Basquete", Sport.BASKETBALL, 150.0, listOf(P_BASKET)),
                        SeedCourt("Quadra 3 - Poliesportiva", Sport.MULTISPORT, 120.0, listOf(P_MULTI))
                    )),
                SeedVenue("Rio Quadras - Tijuca", "Rua Conde de Bonfim, 800", "Rio de Janeiro", "RJ",
                    -22.9240, -43.2330, listOf(P_VOLLEY), 4.5, listOf(
                        SeedCourt("Quadra 1 - Vôlei", Sport.VOLLEYBALL, 100.0, listOf(P_VOLLEY))
                    ))
            )),
        SeedCompany("BH Esporte Total", "Quadras poliesportivas em Belo Horizonte.", P_MULTI,
            listOf(
                SeedVenue("BH Total - Savassi", "Av. do Contorno, 6000", "Belo Horizonte", "MG",
                    -19.9390, -43.9350, listOf(P_MULTI, P_SOCCER), 4.4, listOf(
                        SeedCourt("Quadra 1 - Poliesportiva", Sport.MULTISPORT, 75.0, listOf(P_MULTI)),
                        SeedCourt("Quadra 2 - Society", Sport.SOCCER, 95.0, listOf(P_SOCCER)),
                        SeedCourt("Quadra 3 - Basquete", Sport.BASKETBALL, 85.0, listOf(P_BASKET))
                    ))
            ))
    )
}
