package com.example.services.notifications.impl;

import com.example.domain.entities.Reservation;
import com.example.domain.entities.User;
import com.example.domain.entities.Workspace;
import com.example.services.notifications.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final Logger MAIL_LOGGER = LoggerFactory.getLogger("MAIL_LOGGER");

    @Override
    public void sendReservationConfirmation(Reservation reservation) {

        MAIL_LOGGER.info("Sending reservation confirmation to user {} for workspace {} at {}",
                reservation.getCustomer().getUserId(),
                reservation.getWorkspace().getWorkspaceId(),
                reservation.getReservationCreatedAt());
    }

    @Override
    public void sendWorkspaceConfirmation(Workspace workspace) {
        MAIL_LOGGER.info("Workspace details: Name: {}, ID: {}, Price: {}",
                workspace.getType(),
                workspace.getWorkspaceId(),
                workspace.getPrice());
    }

    @Override
    public void sendUserConfirmation(User user) {
        MAIL_LOGGER.info("User details: Name: {}, User ID: {}, Email: {}",
                user.getFirstName(),
                user.getUserId(),
                user.getEmail());
    }
}
