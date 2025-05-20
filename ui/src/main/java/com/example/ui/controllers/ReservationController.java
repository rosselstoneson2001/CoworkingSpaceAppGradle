package com.example.ui.controllers;

import com.example.entities.Reservation;
import com.example.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing reservations.
 * Provides endpoints to retrieve, create, and delete reservations.
 *
 * <p>Endpoints:</p>
 * <ul>
 *     <li>GET /reservations/get-all - Get all reservations</li>
 *     <li>GET /reservations/get/{id} - Get a reservation by ID</li>
 *     <li>POST /reservations/create - Create a new reservation</li>
 *     <li>DELETE /reservations/remove/{id} - Delete a reservation</li>
 *     <li>GET /reservations/get/customer/{customerName} - Get reservations by customer</li>
 *     <li>GET /reservations/get/workspace/{workspaceId} - Get reservations by workspace</li>
 * </ul>
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;


    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Fetch all reservations.
     *
     * @return List of reservations.
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAll();
        return ResponseEntity.ok(reservations);
    }

    /**
     * Fetch a reservation by ID.
     *
     * @param id Reservation ID.
     * @return Reservation if found, 404 otherwise.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") Long id) {
        Optional<Reservation> reservation = reservationService.getById(id);
        return reservation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new reservation.
     *
     * @param reservation Reservation details.
     * @return Created reservation with 201 status.
     */
    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        reservationService.create(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    /**
     * Delete a reservation by ID.
     *
     * @param id Reservation ID.
     * @return 204 No Content if successful, 404 if not found, 500 for other errors.
     */
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationService.remove(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetch reservations by customer name.
     *
     * @param customerName Name of the customer.
     * @return List of reservations.
     */
    @GetMapping("/get/customer/{customerName}")
    public ResponseEntity<List<Reservation>> getReservationsByCustomer(@PathVariable("customerName") String customerName) {
        List<Reservation> reservations = reservationService.findReservationsByCustomer(customerName);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reservations);
    }

    /**
     * Fetch reservations by workspace ID.
     *
     * @param workspaceId ID of the workspace.
     * @return List of reservations.
     */
    @GetMapping("/get/workspace/{workspaceId}")
    public ResponseEntity<List<Reservation>> getReservationsByWorkspace(@PathVariable("workspaceId") Long workspaceId) {
        List<Reservation> reservations = reservationService.findReservationsByWorkspace(workspaceId);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reservations);
    }
}