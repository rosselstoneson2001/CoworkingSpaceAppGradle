package com.example.services.impl;


import com.example.entities.Reservation;
import com.example.entities.Workspace;
import com.example.exceptions.InvalidReservationException;
import com.example.exceptions.ReservationNotFoundException;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.exceptions.enums.NotFoundErrorCodes;
import com.example.exceptions.enums.ValidationErrorCodes;
import com.example.repositories.ReservationRepository;
import com.example.services.ReservationService;
import com.example.services.WorkspaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles the logic for managing reservations, including creating,
 * retrieving, and removing reservations, as well as checking workspace availability.
 * Implementation of the {@link ReservationService} interface for managing workspace-related operations.
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private final ReservationRepository reservationRepository;
    private final WorkspaceService workspaceService;

    @Autowired
    public ReservationServiceImpl(@Qualifier("jpaReservation") ReservationRepository reservationRepository, WorkspaceService workspaceService) {
        this.reservationRepository = reservationRepository;
        this.workspaceService = workspaceService;
    }

    /**
     * Creates a new reservation for a workspace.
     * Validates that all fields are properly set, that the workspace exists, and that
     * the workspace is available during the specified time.
     *
     * @param reservation the reservation object to be created
     * @throws InvalidReservationException if any required field is missing or if the workspace is unavailable
     * @throws WorkspaceNotFoundException  if the workspace with the given ID does not exist
     */
    @Override
    public void create(Reservation reservation) {

        if (reservation.getWorkspace() == null ||
                reservation.getCustomerName() == null ||
                reservation.getStartDateTime() == null ||
                reservation.getEndDateTime() == null) {
            throw new InvalidReservationException(ValidationErrorCodes.MISSING_FIELD, "All fields are required for booking.");
        }

        if (reservation.getEndDateTime().isBefore(reservation.getStartDateTime())) {
            throw new InvalidReservationException(ValidationErrorCodes.INVALID_DATE, "End date cannot be before start date.");
        }

        Workspace workspace = workspaceService.getWorkspaceWithReservations(reservation.getWorkspace().getWorkspaceId());
        if (workspace == null) {
            throw new WorkspaceNotFoundException(NotFoundErrorCodes.WORKSPACE_NOT_FOUND, "Failed to create reservation. No Workspace found with ID: " + reservation.getWorkspace());
        }

        if (!isWorkspaceAvailable(workspace, reservation.getStartDateTime(), reservation.getEndDateTime())) {
            throw new InvalidReservationException(ValidationErrorCodes.INVALID_DATE, "The workspace is not available for the selected dates.");
        }
        reservationRepository.add(reservation);
    }

    /**
     * Retrieves all reservations from the repository.
     *
     * @return a list of all reservations
     * @throws ReservationNotFoundException if no reservations are found
     */
    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = reservationRepository.getAll();
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException(NotFoundErrorCodes.RESERVATION_NOT_FOUND, "Failed to retrieve all reservations. No Reservations found.");
        } else {
            return reservations;
        }
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param reservationId the ID of the reservation to be retrieved
     * @return an Optional containing the reservation if found, or an empty Optional if not found
     */
    @Override
    public Optional<Reservation> getById(Long reservationId) {
        return reservationRepository.getById(reservationId);
    }

    /**
     * Finds all reservations made by a specific customer.
     *
     * @param customerName the name of the customer
     * @return a list of reservations made by the customer
     * @throws InvalidReservationException  if the customer name is empty
     * @throws ReservationNotFoundException if no reservations are found for the customer
     */
    @Override
    public List<Reservation> findReservationsByCustomer(String customerName) {
        if (customerName.trim().isEmpty()) {
            throw new InvalidReservationException(ValidationErrorCodes.MISSING_FIELD, "Customer name cannot be empty.");
        }

        List<Reservation> reservations = reservationRepository.getReservationsByCustomer(customerName);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException(NotFoundErrorCodes.RESERVATION_NOT_FOUND, "Failed to fin reservation by customer. No reservations found for customer: " + customerName);

        }
        return reservations;
    }

    /**
     * Finds all reservations for a specific workspace.
     *
     * @param workspaceId the ID of the workspace
     * @return a list of reservations for the workspace, or an empty list if none are found
     * @throws ReservationNotFoundException if no reservations are found for the workspace
     */
    @Override
    public List<Reservation> findReservationsByWorkspace(Long workspaceId) {
        List<Reservation> reservations;
        try {
            reservations = reservationRepository.findReservationsByWorkspace(workspaceId);
            if (reservations.isEmpty()) {
                throw new ReservationNotFoundException(NotFoundErrorCodes.RESERVATION_NOT_FOUND, "Failed to retrieve reservation by workspace. No reservations found for workspace ID: " + workspaceId);
            }
        } catch (ReservationNotFoundException e) {
            INTERNAL_LOGGER.error(e.getErrorCode().getCode(), "\"No reservations found \nDetails: " + e.getMessage(), e);
            reservations = new ArrayList<>();
        }
        return reservations;
    }

    /**
     * Removes a reservation by its ID.
     *
     * @param reservationId the ID of the reservation to be removed
     * @throws ReservationNotFoundException if no reservation with the given ID is found
     */
    @Override
    public void remove(Long reservationId) {
        if (getById(reservationId).isPresent()) {
            reservationRepository.remove(reservationId);
        } else {
            throw new ReservationNotFoundException(NotFoundErrorCodes.RESERVATION_NOT_FOUND, "Failed to delete reservation. No reservations found with ID: " + reservationId);
        }
    }

    /**
     * Checks if a workspace is available for the specified time range.
     *
     * @param workspace     the ID of the workspace to check availability for
     * @param startDateTime the start date and time for the reservation
     * @param endDateTime   the end date and time for the reservation
     * @return true if the workspace is available, false otherwise
     */
    @Override
    public boolean isWorkspaceAvailable(Workspace workspace, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Reservation> reservations = workspace.getReservations();

        return reservations.isEmpty() || reservations.stream()
                .noneMatch(r -> startDateTime.isBefore(r.getEndDateTime()) && endDateTime.isAfter(r.getStartDateTime()));
    }
}
