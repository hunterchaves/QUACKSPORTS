package com.example.quacksports

import com.example.quacksports.data.logic.SeedData
import com.example.quacksports.model.Sport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeedDataTest {
    private val seed = SeedData.build()

    @Test fun has_five_companies() {
        assertEquals(5, seed.size)
    }

    @Test fun every_company_has_at_least_one_venue_and_each_venue_has_courts() {
        seed.forEach { c ->
            assertTrue(c.venues.isNotEmpty())
            c.venues.forEach { v -> assertTrue(v.courts.isNotEmpty()) }
        }
    }

    @Test fun all_four_sports_present_across_seed() {
        val sports = seed.flatMap { it.venues }.flatMap { it.courts }.map { it.sport.name }.toSet()
        assertEquals(Sport.entries.map { it.name }.toSet(), sports)
    }

    @Test fun coordinates_and_prices_are_valid() {
        seed.flatMap { it.venues }.forEach { v ->
            assertTrue(v.lat in -34.0..6.0)
            assertTrue(v.lng in -74.0..-34.0)
            v.courts.forEach { assertTrue(it.pricePerHour > 0.0) }
        }
    }
}
