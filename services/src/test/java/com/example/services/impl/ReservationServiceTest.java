package com.example.services.impl;

import com.example.entities.Reservation;
import com.example.entities.Workspace;
import com.example.exceptions.InvalidReservationException;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.repositories.ReservationRepository;
import com.example.repositories.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservationServiceImpl reservationService;

    private WorkspaceRepository workspaceRepository;
    private WorkspaceServiceImpl workspaceService;

    private Reservation reservation;
    private Workspace workspace;

    @BeforeEach
    void setUp() {
//        reservation = new Reservation(1L, "John Doe",
//                LocalDateTime.now().plusDays(1),
//                LocalDateTime.now().plusDays(2),
//                LocalDateTime.now());
//        workspace = new Workspace();

        workspaceRepository = mock(WorkspaceRepository.class);
        workspaceService = new WorkspaceServiceImpl(workspaceRepository);

        reservationRepository = mock(ReservationRepository.class);
        reservationService = new ReservationServiceImpl(reservationRepository, workspaceService);
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        when(workspaceService.findById(any())).thenReturn(Optional.of(workspace));
        when(reservationRepository.findReservationsByWorkspace(any())).thenReturn(List.of());

        assertDoesNotThrow(() -> reservationService.save(reservation));

        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void shouldReturnAllReservations() {
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        assertEquals(reservations, reservationService.findAll());
    }

    @Test
    void shouldFindReservationById() {
        when(reservationRepository.getById(any())).thenReturn(reservation);

        assertEquals(Optional.of(reservation), reservationService.findById(1L));
    }

    @Test
    void shouldRemoveReservation() {
        doNothing().when(reservationRepository).deleteById(any());

        assertDoesNotThrow(() -> reservationService.deleteById(1L));
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    // Exceptions testing

    @Test
    void shouldThrowExceptionWhenFieldsAreMissing() {
        Reservation invalidReservation = new Reservation();

        Exception exception = assertThrows(InvalidReservationException.class, () -> reservationService.save(invalidReservation));
        assertEquals("All fields are required for booking.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEndDateBeforeStartDate() {
        reservation.setEndDateTime(reservation.getStartDateTime().minusDays(1));

        Exception exception = assertThrows(InvalidReservationException.class, () -> reservationService.save(reservation));
        assertEquals("End date cannot be before start date.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenWorkspaceNotFound() {
        when(workspaceService.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(WorkspaceNotFoundException.class, () -> reservationService.save(reservation));
        assertEquals("Workspace with ID 1 not found.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenWorkspaceIsNotAvailable() {
        when(workspaceService.findById(any())).thenReturn(Optional.of(workspace));
        when(reservationRepository.findReservationsByWorkspace(any())).thenReturn(List.of(reservation));

        Exception exception = assertThrows(InvalidReservationException.class, () -> reservationService.save(reservation));
        assertEquals("The workspace is not available for the selected dates.", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenCustomerNameIsEmpty() {
        Exception exception = assertThrows(InvalidReservationException.class, () -> reservationService.findReservationsByCustomer(" "));

        assertEquals("Customer name cannot be empty.", exception.getMessage());
    }

}
