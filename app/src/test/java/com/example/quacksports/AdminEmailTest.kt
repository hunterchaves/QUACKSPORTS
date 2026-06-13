package com.example.quacksports

import com.example.quacksports.data.repository.AuthRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdminEmailTest {
    private val ADMIN = "emanuelchaves199@gmail.com"

    @Test fun exact_admin_email_is_admin() {
        assertTrue(AuthRepository.isAdminEmail(ADMIN))
    }

    @Test fun admin_email_is_case_insensitive() {
        assertTrue(AuthRepository.isAdminEmail("EmanuelChaves199@Gmail.com"))
    }

    @Test fun admin_email_is_trimmed() {
        assertTrue(AuthRepository.isAdminEmail("  emanuelchaves199@gmail.com  "))
    }

    @Test fun regular_email_is_not_admin() {
        assertFalse(AuthRepository.isAdminEmail("higorrosa677@gmail.com"))
    }

    @Test fun null_email_is_not_admin() {
        assertFalse(AuthRepository.isAdminEmail(null))
    }

    @Test fun blank_email_is_not_admin() {
        assertFalse(AuthRepository.isAdminEmail("   "))
    }
}
