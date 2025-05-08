package com.example.api.controllers;

import com.example.api.converters.ReservationConverter;
import com.example.api.dto.requests.ReservationRequestDTO;
import com.example.api.dto.responses.ReservationResponseDTO;
import com.example.api.facade.ReservationFacade;
import com.example.domain.entities.Reservation;
import com.example.services.ReservationService;
import com.example.services.UserService;
import com.example.services.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing reservations.
 * <p>
 * Provides endpoints to create, retrieve, and delete reservations for workspaces.
 * Access to certain endpoints is restricted based on user roles and authorities.
 * </p>
 *
 * <p><b>Endpoints:</b></p>
 * <ul>
 *     <li>GET /reservations/get-all - Get all reservations (Admin only)</li>
 *     <li>GET /reservations/get/{id} - Get reservation by ID</li>
 *     <li>POST /reservations/create - Create a new reservation</li>
 *     <li>DELETE /reservations/remove/{id} - Delete a reservation</li>
 *     <li>GET /reservations/get/customer/{customerName} - Get reservations by customer name (Admin only)</li>
 *     <li>GET /reservations/get/customer-id/reservation - Get reservations of the logged-in customer</li>
 *     <li>GET /reservations/get/workspace/{workspaceId} - Get reservations by workspace ID (Admin only)</li>
 * </ul>
 */

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationFacade reservationFacade;

    private final WorkspaceService workspaceService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, ReservationFacade reservationFacade, WorkspaceService workspaceService, UserService userService) {
        this.reservationService = reservationService;
        this.reservationFacade = reservationFacade;
        this.workspaceService = workspaceService;
        this.userService = userService;
    }

    /**
     * Get a list of all reservations in the system.
     *
     * @return a list of {@link ReservationResponseDTO} for all reservations.
     */
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<Reservation> reservations = reservationService.findAll();
        List<ReservationResponseDTO> reservationResponseDTOS = ReservationConverter.toDTO(reservations);
        return ResponseEntity.ok(reservationResponseDTOS);
    }

    /**
     * Get a reservation by its ID.
     *
     * @param id the reservation ID
     * @return the corresponding {@link ReservationResponseDTO}, or 404 if not found.
     */
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable("id") Long id) {
        Optional<Reservation> reservation = reservationService.findById(id);
        return reservation
                .map(r -> ResponseEntity.ok(ReservationConverter.toDTO(r)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new reservation for a given workspace.
     *
     * @param request the reservation details
     * @return the created reservation with HTTP 201 status
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationRequestDTO request) {
        ReservationResponseDTO response = reservationFacade.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Soft deletes a reservation by marking it inactive.
     *
     * @param id the reservation ID
     * @return HTTP 204 No Content if deleted, 404 if not found
     */
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetch all reservations by a given customer's name.
     * Only accessible to users with the ADMIN role.
     *
     * @param customerName the customer's name
     * @return a list of {@link ReservationResponseDTO} or 404 if none found
     */
    @GetMapping("/get/customer/{customerName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByCustomer(@PathVariable("customerName") String customerName) {
        List<Reservation> reservations = reservationService.findReservationsByCustomer(customerName);
        List<ReservationResponseDTO> reservationResponseDTOS = ReservationConverter.toDTO(reservations);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reservationResponseDTOS);
    }

    /**
     * Fetch all reservations for the currently authenticated customer.
     *
     * @param authentication the security context containing the current user's details
     * @return a list of the current user's reservations, or 404 if none found
     */
    @GetMapping("/get/customer-email/reservation")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByCustomerId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<ReservationResponseDTO> reservations = ReservationConverter
                .toDTO(reservationService.findReservationsByCustomerEmail(user.getUsername()));
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get all reservations linked to a specific workspace.
     *
     * @param workspaceId the workspace ID
     * @return a list of reservations for that workspace, or 404 if none found
     */
    @GetMapping("/get/workspace/{workspaceId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByWorkspace(@PathVariable("workspaceId") Long workspaceId) {
        List<Reservation> reservations = reservationService.findReservationsByWorkspace(workspaceId);
        List<ReservationResponseDTO> reservationResponseDTOS = ReservationConverter.toDTO(reservations);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reservationResponseDTOS);
    }
}