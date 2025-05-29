package com.example.services.impl;

import com.example.domain.entities.Reservation;
import com.example.domain.entities.Workspace;
import com.example.domain.exceptions.InvalidReservationException;
import com.example.domain.exceptions.ReservationNotFoundException;
import com.example.domain.repositories.ReservationRepository;
import com.example.services.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private WorkspaceService workspaceService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Workspace workspace;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        workspace = new Workspace();
        workspace.setWorkspaceId(1L);

        reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setWorkspace(workspace);
        reservation.setCustomerName("John Doe");
        reservation.setStartDateTime(LocalDateTime.now().plusDays(1));
        reservation.setEndDateTime(LocalDateTime.now().plusDays(2));
    }

    @Test
    void testSave_ValidReservation_Success() {
        when(workspaceService.getWorkspaceWithReservations(1L)).thenReturn(workspace);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        assertDoesNotThrow(() -> reservationService.save(reservation));

        verify(reservationRepository, times(1)).save(reservation);
    }


    @Test
    void testFindById_ExistingId_ReturnsReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Optional<Reservation> found = reservationService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(reservation, found.get());
    }

    @Test
    void findAll_shouldReturnReservations_whenReservationsExist() {
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertEquals(2, result.size());
        verify(reservationRepository).findAll();
    }

    @Test
    void findReservationsByCustomerId_shouldReturnReservations_whenFound() {
        String email = "test@example.com";
        List<Reservation> reservations = List.of(new Reservation());
        when(reservationRepository.findReservationsByCustomerEmail(email)).thenReturn(reservations);

        List<Reservation> result = reservationService.findReservationsByCustomerEmail(email);

        assertEquals(1, result.size());
        verify(reservationRepository).findReservationsByCustomerEmail(email);
    }

    @Test
    void findReservationsByWorkspace_shouldReturnReservations_whenFound() {
        Long workspaceId = 1L;
        List<Reservation> reservations = List.of(new Reservation());
        when(reservationRepository.findReservationsByWorkspace(workspaceId)).thenReturn(reservations);

        List<Reservation> result = reservationService.findReservationsByWorkspace(workspaceId);

        assertEquals(1, result.size());
        verify(reservationRepository).findReservationsByWorkspace(workspaceId);
    }

    // --- EXCEPTION CASES ---

    @Test
    void testSave_NullFields_ThrowsException() {
        reservation.setCustomerName(null);

        InvalidReservationException exception = assertThrows(InvalidReservationException.class, () -> reservationService.save(reservation));
        assertEquals("All fields are required for booking.", exception.getMessage());
    }

    @Test
    void testSave_EndBeforeStart_ThrowsException() {
        reservation.setEndDateTime(LocalDateTime.now().plusDays(1));
        reservation.setStartDateTime(LocalDateTime.now().plusDays(2));

        InvalidReservationException exception = assertThrows(InvalidReservationException.class, () -> reservationService.save(reservation));
        assertEquals("End date cannot be before start date.", exception.getMessage());
    }

    @Test
    void testFindAll_NoReservations_ThrowsException() {
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.findAll());
    }

    @Test
    void testFindReservationsByCustomer_EmptyName_ThrowsException() {
        assertThrows(InvalidReservationException.class, () -> reservationService.findReservationsByCustomer(" "));
    }

    @Test
    void testDeleteById_NotExisting_ThrowsException() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.deleteById(99L));
    }

    // True & False

    @Test
    void testIsWorkspaceAvailable_NoConflicts_ReturnsTrue() {
        workspace.setReservations(Collections.emptyList());

        boolean available = reservationService.isWorkspaceAvailable(workspace, reservation.getStartDateTime(), reservation.getEndDateTime());

        assertTrue(available);
    }

    @Test
    void testIsWorkspaceAvailable_WithConflicts_ReturnsFalse() {
        Reservation existingReservation = new Reservation();
        existingReservation.setStartDateTime(LocalDateTime.now().plusDays(1));
        existingReservation.setEndDateTime(LocalDateTime.now().plusDays(3));
        workspace.setReservations(List.of(existingReservation));

        boolean available = reservationService.isWorkspaceAvailable(workspace, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4));

        assertFalse(available);
    }
}
