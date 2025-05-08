package com.example.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WorkspaceTest {

    private Workspace workspace;

    @BeforeEach
    void setUp() {
        workspace = new Workspace();
        workspace.setWorkspaceId(1L);
        workspace.setType("Private Office");
        workspace.setPrice(new BigDecimal("100.00"));
    }

    @Test
    void testConstructorAndGetter() {
        Workspace workspace = new Workspace(
                1L,
                "Private Office",
                new BigDecimal("100.00"),
                true,
                new ArrayList<>());

        assertEquals(1L, workspace.getWorkspaceId());
        assertEquals("Private Office", workspace.getType());
        assertEquals(new BigDecimal("100.00"), workspace.getPrice());
        assertTrue(workspace.isActive());
    }

    @Test
    void testSetters() {
        workspace.setType("Coworking Space");
        workspace.setPrice(new BigDecimal("150.00"));

        assertEquals("Coworking Space", workspace.getType());
        assertEquals(new BigDecimal("150.00"), workspace.getPrice());
    }

    @Test
    void testDefaultValues() {
        assertTrue(workspace.isActive());
        assertNotNull(workspace.getReservations());
        assertTrue(workspace.getReservations().isEmpty());
    }

    @Test
    void testReservations() {
        Reservation reservation = new Reservation();
        reservation.setWorkspace(workspace);

        workspace.getReservations().add(reservation);

        assertEquals(1, workspace.getReservations().size());
        assertTrue(workspace.getReservations().contains(reservation));
    }

}
