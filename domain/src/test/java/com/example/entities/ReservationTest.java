package com.example.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReservationTest {

    @Test
    void testReservationCreation() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusDays(1);
        LocalDateTime endDateTime = now.plusDays(2);

        Workspace workspace = new Workspace();
        workspace.setWorkspaceId(1L);
        workspace.setType("Meeting Room");
        workspace.setPrice(BigDecimal.valueOf(150.00));

        Reservation reservation = new Reservation(
                workspace,
                "John Doe",
                startDateTime,
                endDateTime,
                now
        );

        assertNull(reservation.getReservationId()); // ID is not initialized
        assertEquals(workspace, reservation.getWorkspace());
        assertEquals("John Doe", reservation.getCustomerName());
        assertEquals(startDateTime, reservation.getStartDateTime());
        assertEquals(endDateTime, reservation.getEndDateTime());
        assertEquals(now, reservation.getReservationCreatedAt());
    }

    @Test
    void testReservationSetters() {
        Reservation reservation = new Reservation();

        Workspace workspace = new Workspace();
        workspace.setWorkspaceId(2L);
        workspace.setType("Private Office");
        workspace.setPrice(BigDecimal.valueOf(100.00));

        reservation.setReservationId(100L);
        reservation.setWorkspace(workspace);
        reservation.setCustomerName("Alice Smith");
        LocalDateTime start = LocalDateTime.of(2025, 5, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 5, 1, 12, 0);
        LocalDateTime createdAt = LocalDateTime.now();

        reservation.setStartDateTime(start);
        reservation.setEndDateTime(end);
        reservation.setReservationCreatedAt(createdAt);

        assertEquals(100L, reservation.getReservationId());
        assertEquals(2L, reservation.getWorkspace().getWorkspaceId());
        assertEquals("Alice Smith", reservation.getCustomerName());
        assertEquals(start, reservation.getStartDateTime());
        assertEquals(end, reservation.getEndDateTime());
        assertEquals(createdAt, reservation.getReservationCreatedAt());
    }
}
