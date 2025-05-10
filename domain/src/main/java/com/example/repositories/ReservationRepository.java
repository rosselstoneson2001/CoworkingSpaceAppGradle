package com.example.repositories;

import com.example.entities.Reservation;

import java.util.List;
/**
 * Repository interface for managing {@link Reservation} entities.
 * <p>
 * This interface defines methods for accessing and manipulating reservation data
 * from the data storage (e.g., database or file system). It extends the
 * {@link CrudRepository} to inherit basic CRUD operations for the {@link Reservation} entity.
 * </p>
 */
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    List<Reservation> getReservationsByCustomer(String customerName);

    List<Reservation> findReservationsByWorkspace(Long workspaceId);

}
