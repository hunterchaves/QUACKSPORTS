package com.example.quacksports

import com.example.quacksports.model.Sport
import org.junit.Assert.assertEquals
import org.junit.Test

class SportTest {
    @Test fun from_maps_known_names() {
        assertEquals(Sport.SOCCER, Sport.from("SOCCER"))
        assertEquals(Sport.BASKETBALL, Sport.from("BASKETBALL"))
        assertEquals(Sport.VOLLEYBALL, Sport.from("VOLLEYBALL"))
        assertEquals(Sport.MULTISPORT, Sport.from("MULTISPORT"))
    }

    @Test fun from_null_falls_back_to_multisport() {
        assertEquals(Sport.MULTISPORT, Sport.from(null))
    }

    @Test fun from_unknown_falls_back_to_multisport() {
        assertEquals(Sport.MULTISPORT, Sport.from("CHESS"))
    }

    @Test fun labels_are_in_portuguese() {
        assertEquals("Futebol", Sport.SOCCER.label)
        assertEquals("Basquete", Sport.BASKETBALL.label)
        assertEquals("Vôlei", Sport.VOLLEYBALL.label)
        assertEquals("Poliesportiva", Sport.MULTISPORT.label)
    }
}
