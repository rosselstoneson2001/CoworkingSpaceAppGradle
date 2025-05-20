package com.example.ui.controllers;

import com.example.converters.ReservationConverter;
import com.example.dto.requests.ReservationRequestDTO;
import com.example.dto.responses.ReservationResponseDTO;
import com.example.entities.Reservation;
import com.example.entities.Workspace;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.exceptions.enums.NotFoundErrorCodes;
import com.example.services.ReservationService;
import com.example.services.WorkspaceService;
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
    private final WorkspaceService workspaceService;


    public ReservationController(ReservationService reservationService, WorkspaceService workspaceService) {
        this.reservationService = reservationService;
        this.workspaceService = workspaceService;
    }

    /**
     * Fetch all reservations.
     *
     * @return List of reservations.
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<Reservation> reservations = reservationService.findAll();
        List<ReservationResponseDTO> reservationResponseDTOS = ReservationConverter.toDTO(reservations);
        return ResponseEntity.ok(reservationResponseDTOS);
    }

    /**
     * Fetch a reservation by ID.
     *
     * @param id Reservation ID.
     * @return Reservation if found, 404 otherwise.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable("id") Long id) {
        Optional<Reservation> reservation = reservationService.findById(id);
        return reservation
                .map(r -> ResponseEntity.ok(ReservationConverter.toDTO(r)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new reservation.
     *
     * @param request Reservation details.
     * @return Created reservation with 201 status.
     */
    @PostMapping("/create")
    public ResponseEntity<ReservationRequestDTO> createReservation(@RequestBody ReservationRequestDTO request) {
       Optional<Workspace> workspaceOptional = workspaceService.findById(request.getWorkspaceId());
       Workspace workspace = workspaceOptional.orElseThrow(() -> new WorkspaceNotFoundException(
               NotFoundErrorCodes.WORKSPACE_NOT_FOUND,
               "No Workspace found with ID: " + request.getWorkspaceId()
       ));

        Reservation reservation = ReservationConverter.toEntity(request, workspace);
        reservationService.save(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    /**
     * Delete a reservation by ID.
     *
     * @param id Reservation ID.
     * @return 204 No Content if successful, 404 if not found, 500 for other errors.
     */
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetch reservations by customer name.
     *
     * @param customerName Name of the customer.
     * @return List of reservations.
     */
    @GetMapping("/get/customer/{customerName}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByCustomer(@PathVariable("customerName") String customerName) {
        List<Reservation> reservations = reservationService.findReservationsByCustomer(customerName);
        List<ReservationResponseDTO> reservationResponseDTOS = ReservationConverter.toDTO(reservations);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reservationResponseDTOS);
    }

    /**
     * Fetch reservations by workspace ID.
     *
     * @param workspaceId ID of the workspace.
     * @return List of reservations.
     */
    @GetMapping("/get/workspace/{workspaceId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByWorkspace(@PathVariable("workspaceId") Long workspaceId) {
        List<Reservation> reservations = reservationService.findReservationsByWorkspace(workspaceId);
        List<ReservationResponseDTO> reservationResponseDTOS = ReservationConverter.toDTO(reservations);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reservationResponseDTOS);
    }
}