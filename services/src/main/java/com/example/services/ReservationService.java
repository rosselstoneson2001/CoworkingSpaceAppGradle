package com.example.services;


import com.example.domain.entities.Reservation;
import com.example.domain.entities.Workspace;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for managing reservations.
 * <p>
 * Extends the {@link CrudService} interface to provide basic CRUD operations for reservations.
 * Provides additional methods specific to handling reservations, such as searching by customer
 * or workspace and checking availability.
 * </p>
 */
public interface ReservationService extends CrudService<Reservation, Long> {

    List<Reservation> findReservationsByCustomer(String customerName);

    List<Reservation> findReservationsByWorkspace(Long workspaceId);

    List<Reservation> findReservationsByCustomerEmail(String email);

    boolean isWorkspaceAvailable(Workspace workspace, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
