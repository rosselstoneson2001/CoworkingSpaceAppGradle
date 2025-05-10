package com.example.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReservationTest {

    @Test
    void testReservationCreation() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusDays(1);
        LocalDateTime endDateTime = now.plusDays(2);


        Reservation reservation = new Reservation(
                1L,
                "John Doe",
                startDateTime,
                endDateTime,
                now
        );

        assertNull(reservation.getReservationId()); // ID is not initialized
        assertEquals(1L, reservation.getWorkspaceId());
        assertEquals("John Doe", reservation.getCustomerName());
        assertEquals(startDateTime, reservation.getStartDateTime());
        assertEquals(endDateTime, reservation.getEndDateTime());
        assertEquals(now, reservation.getReservationCreatedAt());
    }

    @Test
    void testReservationSetters() {
        Reservation reservation = new Reservation();

        reservation.setReservationId(100L);
        reservation.setWorkspaceId(2L);
        reservation.setCustomerName("Alice Smith");
        LocalDateTime start = LocalDateTime.of(2025, 5, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 5, 1, 12, 0);
        LocalDateTime createdAt = LocalDateTime.now();

        reservation.setStartDateTime(start);
        reservation.setEndDateTime(end);
        reservation.setReservationCreatedAt(createdAt);

        assertEquals(100L, reservation.getReservationId());
        assertEquals(2L, reservation.getWorkspaceId());
        assertEquals("Alice Smith", reservation.getCustomerName());
        assertEquals(start, reservation.getStartDateTime());
        assertEquals(end, reservation.getEndDateTime());
        assertEquals(createdAt, reservation.getReservationCreatedAt());
    }
}
