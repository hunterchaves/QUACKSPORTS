package com.example.quacksports

import com.example.quacksports.data.logic.Availability
import com.example.quacksports.model.Reservation
import org.junit.Assert.assertEquals
import org.junit.Test

class AvailabilityTest {
    private fun res(start: Int, end: Int, status: String = "CONFIRMED") =
        Reservation(courtId = "c1", date = "2026-06-10", startHour = start, endHour = end, status = status)

    @Test fun slots_span_open_to_close_exclusive_of_close() {
        val slots = Availability.slotsFor(openHour = 8, closeHour = 11, reservations = emptyList())
        assertEquals(listOf(8, 9, 10), slots.map { it.hour })
        assertEquals(true, slots.all { it.available })
    }

    @Test fun occupied_hours_marked_unavailable() {
        val slots = Availability.slotsFor(8, 12, listOf(res(9, 10), res(10, 11)))
        assertEquals(mapOf(8 to true, 9 to false, 10 to false, 11 to true),
            slots.associate { it.hour to it.available })
    }

    @Test fun cancelled_reservations_do_not_block() {
        val slots = Availability.slotsFor(8, 10, listOf(res(8, 9, status = "CANCELLED")))
        assertEquals(true, slots.first { it.hour == 8 }.available)
    }

    @Test fun single_reservation_spanning_multiple_hours_blocks_each_hour() {
        val slots = Availability.slotsFor(8, 13, listOf(res(9, 12)))
        assertEquals(mapOf(8 to true, 9 to false, 10 to false, 11 to false, 12 to true),
            slots.associate { it.hour to it.available })
    }

    @Test fun open_equal_to_close_yields_no_slots() {
        assertEquals(emptyList<Int>(), Availability.slotsFor(8, 8, emptyList()).map { it.hour })
    }

    @Test fun open_after_close_yields_no_slots() {
        assertEquals(emptyList<Int>(), Availability.slotsFor(12, 8, emptyList()).map { it.hour })
    }

    @Test fun non_confirmed_status_does_not_block() {
        val slots = Availability.slotsFor(8, 10, listOf(res(8, 9, status = "PENDING")))
        assertEquals(true, slots.first { it.hour == 8 }.available)
    }
}
