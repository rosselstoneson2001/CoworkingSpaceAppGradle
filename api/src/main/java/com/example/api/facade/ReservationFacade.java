package com.example.api.facade;

import com.example.api.converters.ReservationConverter;
import com.example.api.dto.requests.ReservationRequestDTO;
import com.example.api.dto.responses.ReservationResponseDTO;
import com.example.domain.entities.Reservation;
import com.example.domain.entities.User;
import com.example.domain.entities.Workspace;
import com.example.domain.exceptions.UserNotFoundException;
import com.example.domain.exceptions.WorkspaceNotFoundException;
import com.example.domain.exceptions.enums.NotFoundErrorCodes;
import com.example.services.ReservationService;
import com.example.services.UserService;
import com.example.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Facade class for handling reservation-related operations.
 *
 * This class coordinates between the UserService, WorkspaceService, and ReservationService
 * to perform the necessary operations to create a reservation.
 * It abstracts the complexities of these services and provides a simplified interface
 * for the rest of the application.
 *
 * It includes functionality to:
 * 1. Retrieve a user by ID.
 * 2. Retrieve a workspace by ID.
 * 3. Convert the input DTO to an entity.
 * 4. Save the reservation.
 * 5. Return a DTO with reservation details.
 */
@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final UserService userService;
    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;

    /**
     * Creates a reservation by first validating the user and workspace,
     * then converting the DTO to an entity, saving it, and returning the response DTO.
     *
     * @param requestDto the reservation request data transfer object containing necessary details
     * @return a {@link ReservationResponseDTO} containing the reservation details
     * @throws UserNotFoundException if the user cannot be found with the provided ID
     * @throws WorkspaceNotFoundException if the workspace cannot be found with the provided ID
     */
    public ReservationResponseDTO createReservation(ReservationRequestDTO requestDto) {
        User customer = userService.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(NotFoundErrorCodes.USER_NOT_FOUND, "User not found with ID: " + requestDto.getUserId()));

        Workspace workspace = workspaceService.findById(requestDto.getWorkspaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException(NotFoundErrorCodes.WORKSPACE_NOT_FOUND, "Workspace not found with ID: " + requestDto.getWorkspaceId()));

        Reservation reservation = ReservationConverter.toEntity(requestDto, workspace, customer);

        reservationService.save(reservation);

        return ReservationConverter.toDTO(reservation);
    }

}
