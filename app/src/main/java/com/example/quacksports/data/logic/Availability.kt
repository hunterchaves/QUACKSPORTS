package com.example.quacksports.data.logic

import com.example.quacksports.model.Reservation
import com.example.quacksports.model.TimeSlot

object Availability {
    fun slotsFor(openHour: Int, closeHour: Int, reservations: List<Reservation>): List<TimeSlot> {
        val taken = reservations
            .filter { it.status == "CONFIRMED" }
            .flatMap { it.startHour until it.endHour }
            .toSet()
        return (openHour until closeHour).map { h -> TimeSlot(h, h !in taken) }
    }
}
