package com.example.services.impl;


import com.example.entities.Reservation;
import com.example.entities.Workspace;
import com.example.exceptions.InvalidReservationException;
import com.example.exceptions.ReservationNotFoundException;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.ReservationRepository;
import com.example.services.ReservationService;
import com.example.services.WorkspaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReservationServiceImpl implements ReservationService {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");
    private final ReservationRepository reservationRepository;
    private final WorkspaceService workspaceService;

    public ReservationServiceImpl(ReservationRepository reservationRepository, WorkspaceService workspaceService) {
        this.reservationRepository = reservationRepository;
        this.workspaceService = workspaceService;
    }

    @Override
    public void create(Reservation reservation) {

        if (reservation.getWorkspaceId() == null ||
                reservation.getCustomerName() == null ||
                reservation.getStartDateTime() == null ||
                reservation.getEndDateTime() == null) {
            throw new InvalidReservationException(ErrorCodes.INVALID_RESERVATION, "All fields are required for booking.");
        }

        if (reservation.getEndDateTime().isBefore(reservation.getStartDateTime())) {
            throw new InvalidReservationException(ErrorCodes.INVALID_RESERVATION, "End date cannot be before start date.");
        }

        Optional<Workspace> workspace = workspaceService.getById(reservation.getWorkspaceId());
        if (workspace.isEmpty()) {
            throw new WorkspaceNotFoundException(ErrorCodes.WORKSPACE_NOT_FOUND, "No Workspace found with ID: " + reservation.getWorkspaceId());
        }

        if (isWorkspaceAvailable(reservation.getWorkspaceId(), reservation.getStartDateTime(), reservation.getEndDateTime())) {
            reservationRepository.add(reservation);
        } else {
            throw new InvalidReservationException(ErrorCodes.INVALID_RESERVATION, "The workspace is not available for the selected dates.");
        }
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = reservationRepository.getAll();
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException(ErrorCodes.RESERVATION_NOT_FOUND, "No Reservations found.");
        } else {
            return reservations;
        }
    }

    @Override
    public Optional<Reservation> getById(Long reservationId) {
        return reservationRepository.getById(reservationId);
    }

    @Override
    public List<Reservation> findReservationsByCustomer(String customerName) {
        if (customerName.trim().isEmpty()) {
            throw new InvalidReservationException(ErrorCodes.INVALID_WORKSPACE, "Customer name cannot be empty.");
        }

        List<Reservation> reservations = reservationRepository.getReservationsByCustomer(customerName);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException(ErrorCodes.RESERVATION_NOT_FOUND, "No reservations found for customer: " + customerName);

        }
        return reservations;
    }

    @Override
    public List<Reservation> findReservationsByWorkspace(Long workspaceId) {
        List<Reservation> reservations = reservationRepository.findReservationsByWorkspace(workspaceId);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException(ErrorCodes.RESERVATION_NOT_FOUND, "No reservations found for workspace ID: " + workspaceId);
        }
        return reservations;
    }

    @Override
    public void remove(Long reservationId) {
        if (getById(reservationId).isPresent()) {
            reservationRepository.remove(reservationId);
        } else {
            throw new ReservationNotFoundException(ErrorCodes.RESERVATION_NOT_FOUND, "No reservations found with ID: " + reservationId);
        }
    }

//    @Override
//    public void save() {
//        reservationRepository.save();
//    }
//
//    @Override
//    public void load() {
//        reservationRepository.load();
//    }

    @Override
    public boolean isWorkspaceAvailable(Long workspaceId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Reservation> reservations = findReservationsByWorkspace(workspaceId);

        if (reservations.isEmpty()) {
            return true;
        }

        return reservations.stream()
                .noneMatch(r -> !(endDateTime.isBefore(r.getStartDateTime()) || startDateTime.isAfter(r.getEndDateTime())));
    }
}
