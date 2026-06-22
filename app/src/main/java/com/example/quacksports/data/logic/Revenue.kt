package com.example.quacksports.data.logic

import com.example.quacksports.model.Payment
import com.example.quacksports.model.Reservation

object Revenue {
    fun total(payments: List<Payment>): Double =
        payments.filter { it.status == "APPROVED" }.sumOf { it.amount }

    fun perCompany(reservations: List<Reservation>): Map<String, Double> =
        reservations.filter { it.status == "CONFIRMED" }
            .groupBy { it.companyId }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
}
