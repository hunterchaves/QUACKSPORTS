package com.example.quacksports.model

enum class Sport(val label: String) {
    SOCCER("Futebol"),
    BASKETBALL("Basquete"),
    VOLLEYBALL("Vôlei"),
    MULTISPORT("Poliesportiva");
    companion object { fun from(s: String?) = entries.firstOrNull { it.name == s } ?: MULTISPORT }
}
