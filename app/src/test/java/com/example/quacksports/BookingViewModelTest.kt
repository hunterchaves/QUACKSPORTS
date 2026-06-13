package com.example.quacksports

import com.example.quacksports.data.repository.ReservationRepository
import com.example.quacksports.data.repository.VenueRepository
import com.example.quacksports.model.Court
import com.example.quacksports.model.Reservation
import com.example.quacksports.model.Venue
import com.example.quacksports.ui.viewmodel.BookingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class BookingViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val venueRepo: VenueRepository = mock()
    private val reservationRepo: ReservationRepository = mock()

    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test fun load_populates_venue_and_court() = runTest(dispatcher) {
        val court = Court(id = "c1", venueId = "v1", openHour = 8, closeHour = 11)
        whenever(venueRepo.getVenue("v1")).thenReturn(Venue(id = "v1", name = "Arena"))
        whenever(venueRepo.getCourt("v1", "c1")).thenReturn(court)

        val vm = BookingViewModel(venueRepo, reservationRepo)
        vm.load("v1", "c1")
        advanceUntilIdle()

        assertEquals("Arena", vm.venue?.name)
        assertEquals("c1", vm.court?.id)
    }

    @Test fun pickDate_marks_taken_hours_unavailable() = runTest(dispatcher) {
        val court = Court(id = "c1", venueId = "v1", openHour = 8, closeHour = 11)
        whenever(venueRepo.getVenue("v1")).thenReturn(Venue(id = "v1"))
        whenever(venueRepo.getCourt("v1", "c1")).thenReturn(court)
        whenever(reservationRepo.forCourtOnDate("c1", "2026-06-10")).thenReturn(
            listOf(Reservation(courtId = "c1", date = "2026-06-10", startHour = 9, endHour = 10, status = "CONFIRMED"))
        )

        val vm = BookingViewModel(venueRepo, reservationRepo)
        vm.load("v1", "c1")
        advanceUntilIdle()
        vm.pickDate("2026-06-10")
        advanceUntilIdle()

        assertEquals("2026-06-10", vm.selectedDate)
        assertEquals(
            mapOf(8 to true, 9 to false, 10 to true),
            vm.slots.associate { it.hour to it.available }
        )
    }

    @Test fun pickDate_without_loaded_court_does_not_query_reservations() = runTest(dispatcher) {
        val vm = BookingViewModel(venueRepo, reservationRepo)
        vm.pickDate("2026-06-10")
        advanceUntilIdle()

        assertEquals("2026-06-10", vm.selectedDate)
        assertTrue(vm.slots.isEmpty())
        verify(reservationRepo, never()).forCourtOnDate(org.mockito.kotlin.any(), org.mockito.kotlin.any())
    }
}
