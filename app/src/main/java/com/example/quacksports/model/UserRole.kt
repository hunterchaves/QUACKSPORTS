package com.example.quacksports.model

enum class UserRole { USER, COMPANY, ADMIN;
    companion object { fun from(s: String?) = entries.firstOrNull { it.name == s } ?: USER }
}
