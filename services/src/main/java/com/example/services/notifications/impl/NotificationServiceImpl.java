package com.example.services.notifications.impl;

import com.example.domain.entities.Reservation;
import com.example.domain.entities.User;
import com.example.domain.entities.Workspace;
import com.example.services.notifications.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link NotificationService} interface that handles
 * the sending of various confirmation notifications (reservation, user, workspace).
 *
 * This service uses {@link Logger} to log the information regarding the sending
 * of these notifications. It currently logs the details but could be extended
 * to send actual email notifications or messages.
 *
 * The methods in this service log confirmation messages related to reservations,
 * workspace details, and user details.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final Logger MAIL_LOGGER = LoggerFactory.getLogger("MAIL_LOGGER");

    /**
     * Sends a reservation confirmation notification.
     *
     * Logs information related to the reservation, including user ID, workspace ID,
     * and reservation creation timestamp. This could be extended to send an actual
     * email or notification.
     *
     * @param reservation the reservation for which the confirmation is to be sent
     */
    @Override
    public void sendReservationConfirmation(Reservation reservation) {

        MAIL_LOGGER.info("Sending reservation confirmation to user {} for workspace {} at {}",
                reservation.getCustomer().getUserId(),
                reservation.getWorkspace().getWorkspaceId(),
                reservation.getReservationCreatedAt());
    }

    /**
     * Sends a workspace confirmation notification.
     *
     * Logs workspace details, such as name, ID, and price. This could be extended
     * to send an actual email or notification.
     *
     * @param workspace the workspace for which the confirmation is to be sent
     */
    @Override
    public void sendWorkspaceConfirmation(Workspace workspace) {
        MAIL_LOGGER.info("Workspace details: Name: {}, ID: {}, Price: {}",
                workspace.getType(),
                workspace.getWorkspaceId(),
                workspace.getPrice());
    }

    /**
     * Sends a user confirmation notification.
     *
     * Logs user details, such as first name, user ID, and email address. This could
     * be extended to send an actual email or notification.
     *
     * @param user the user for whom the confirmation is to be sent
     */
    @Override
    public void sendUserConfirmation(User user) {
        MAIL_LOGGER.info("User details: Name: {}, User ID: {}, Email: {}",
                user.getFirstName(),
                user.getUserId(),
                user.getEmail());
    }
}
