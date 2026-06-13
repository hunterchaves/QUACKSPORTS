package com.example.quacksports

import com.example.quacksports.data.logic.Revenue
import com.example.quacksports.model.Payment
import org.junit.Assert.assertEquals
import org.junit.Test

class RevenueTest {
    private val payments = listOf(
        Payment(id = "p1", amount = 100.0, status = "APPROVED"),
        Payment(id = "p2", amount = 50.0, status = "APPROVED"),
        Payment(id = "p3", amount = 999.0, status = "FAILED")
    )

    @Test fun total_sums_only_approved() {
        assertEquals(150.0, Revenue.total(payments), 0.001)
    }

    @Test fun perCompany_groups_reservation_amounts() {
        val reservations = listOf(
            com.example.quacksports.model.Reservation(companyId = "a", amount = 80.0, status = "CONFIRMED"),
            com.example.quacksports.model.Reservation(companyId = "a", amount = 20.0, status = "CONFIRMED"),
            com.example.quacksports.model.Reservation(companyId = "b", amount = 30.0, status = "CONFIRMED"),
            com.example.quacksports.model.Reservation(companyId = "b", amount = 99.0, status = "CANCELLED")
        )
        assertEquals(mapOf("a" to 100.0, "b" to 30.0), Revenue.perCompany(reservations))
    }

    @Test fun total_of_empty_list_is_zero() {
        assertEquals(0.0, Revenue.total(emptyList()), 0.001)
    }

    @Test fun total_with_no_approved_payments_is_zero() {
        val none = listOf(
            Payment(id = "p1", amount = 100.0, status = "FAILED"),
            Payment(id = "p2", amount = 50.0, status = "PENDING")
        )
        assertEquals(0.0, Revenue.total(none), 0.001)
    }

    @Test fun perCompany_of_empty_list_is_empty_map() {
        assertEquals(emptyMap<String, Double>(), Revenue.perCompany(emptyList()))
    }
}
