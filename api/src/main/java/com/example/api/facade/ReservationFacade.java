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
import com.example.services.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final UserService userService;
    private final WorkspaceService workspaceService;
    private final ReservationService reservationService;
    private final NotificationService notificationService;

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
