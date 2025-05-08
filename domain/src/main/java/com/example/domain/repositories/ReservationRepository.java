package com.example.domain.repositories;

import com.example.domain.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repository interface for managing {@link Reservation} entities.
 * <p>
 * This interface extends {@link JpaRepository}, which provides basic CRUD operations
 * and additional JPA-related functionality for interacting with the database. It is
 * specifically designed for the {@link Reservation} entity and allows for easy
 * management of reservations.
 * </p>
 *
 * <p>
 * The repository provides custom query methods to fetch reservations based on customer
 * names, workspace IDs, and update reservation statuses.
 * </p>
 *
 * <p>
 * The interface includes the following custom methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #getReservationsByCustomer(String)} – Get active reservations by customer name.</li>
 *     <li>{@link #findReservationsByWorkspace(Long)} – Get active reservations by workspace ID.</li>
 *     <li>{@link #findReservationsByCustomerEmail(String)} – Get active reservations by customer email.</li>
 *     <li>{@link #deleteById(Long)} – Soft-deletes a reservation.</li>
 * </ul>
 *
 * <p>
 * The {@link #deleteById(Long)} method is marked with {@link Modifying}, indicating that it performs
 * an update operation in the database.
 * </p>
 *
 * @see Reservation
 * @see JpaRepository
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Fetches a list of active reservations for a specific customer.
     *
     * @param customerName the name of the customer whose reservations are to be retrieved
     * @return a list of active {@link Reservation} entities for the given customer name, or all reservations with null customer names
     */
    @Query("SELECT r FROM reservation r WHERE (r.customerName = :customerName OR r.customerName IS NULL) AND r.isActive = true")
    List<Reservation> getReservationsByCustomer(String customerName);

    /**
     * Fetches a list of active reservations for a specific workspace.
     *
     * @param workspaceId the ID of the workspace whose reservations are to be retrieved
     * @return a list of active {@link Reservation} entities for the given workspace ID, or all reservations with null workspace IDs
     */
    @Query("SELECT r FROM reservation r WHERE (r.workspace = :workspaceId OR r.workspace IS NULL) AND r.isActive = true")
    List<Reservation> findReservationsByWorkspace(Long workspaceId);

    /**
     * Fetches active reservations for a given customer email.
     *
     * @param email the email of the customer
     * @return list of active reservations
     */
    @Query("SELECT r FROM reservation r WHERE (r.customer.email = :email) AND r.isActive = true")
    List<Reservation> findReservationsByCustomerEmail(String email);

    /**
     * Marks a reservation as inactive (soft delete) by its ID.
     *
     * @param id the ID of the reservation to be marked as inactive
     */
    @Modifying
    @Query("UPDATE reservation r SET r.isActive = false WHERE r.reservationId = :id")
    @Override
    void deleteById(@Param("id") Long id);

}
