package com.example.quacksports

import com.example.quacksports.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Test

class UserRoleTest {
    @Test fun from_maps_known_names() {
        assertEquals(UserRole.USER, UserRole.from("USER"))
        assertEquals(UserRole.COMPANY, UserRole.from("COMPANY"))
        assertEquals(UserRole.ADMIN, UserRole.from("ADMIN"))
    }

    @Test fun from_null_falls_back_to_user() {
        assertEquals(UserRole.USER, UserRole.from(null))
    }

    @Test fun from_unknown_falls_back_to_user() {
        assertEquals(UserRole.USER, UserRole.from("SUPERADMIN"))
    }

    @Test fun from_is_case_sensitive_and_falls_back() {
        assertEquals(UserRole.USER, UserRole.from("admin"))
    }
}
