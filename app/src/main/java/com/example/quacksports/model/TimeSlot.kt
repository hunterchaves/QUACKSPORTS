package com.example.quacksports.model

data class TimeSlot(val hour: Int, val available: Boolean) {
    val label: String get() = "%02d:00".format(hour)
}
