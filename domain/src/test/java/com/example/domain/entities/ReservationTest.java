package com.example.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    private Reservation reservation;
    private Workspace workspace;
    private User user;

    @BeforeEach
    void setUp() {
        workspace = new Workspace();
        workspace.setWorkspaceId(1L);
        workspace.setType("Private Office");

        user = new User();
        user.setUserId(1L);
        user.setFirstName("john");
        user.setLastName("doe");

        reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setCustomerName("John Doe");
        reservation.setStartDateTime(LocalDateTime.of(2025, 4, 10, 10, 0));
        reservation.setEndDateTime(LocalDateTime.of(2025, 4, 10, 12, 0));
        reservation.setWorkspace(workspace);
        reservation.setCustomer(user);
    }

    @Test
    void testConstructorAndGetters() {
        LocalDateTime start = LocalDateTime.of(2025, 5, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2025, 5, 1, 11, 0);

        Reservation newReservation = new Reservation(
                2L,
                "Jane Doe",
                start,
                end,
                null, // createdAt is managed by Hibernate
                true,
                workspace,
                user
        );

        assertEquals(2L, newReservation.getReservationId());
        assertEquals("Jane Doe", newReservation.getCustomerName());
        assertEquals(start, newReservation.getStartDateTime());
        assertEquals(end, newReservation.getEndDateTime());
        assertTrue(newReservation.isActive());
        assertEquals(workspace, newReservation.getWorkspace());
        assertEquals(user, newReservation.getCustomer());
    }

    @Test
    void testSetters() {
        reservation.setCustomerName("Updated Name");
        assertEquals("Updated Name", reservation.getCustomerName());

        LocalDateTime newStart = LocalDateTime.of(2025, 4, 11, 14, 0);
        reservation.setStartDateTime(newStart);
        assertEquals(newStart, reservation.getStartDateTime());

        reservation.setActive(false);
        assertFalse(reservation.isActive());
    }

    @Test
    void testWorkspaceAndCustomerAssociation() {
        assertEquals(workspace, reservation.getWorkspace());
        assertEquals(user, reservation.getCustomer());
    }
}
