package com.example.services.notifications;

import com.example.domain.entities.Reservation;
import com.example.domain.entities.User;
import com.example.domain.entities.Workspace;

/**
 * Interface defining the contract for sending notifications in the system.
 *
 * This interface provides methods for sending various types of notifications
 * related to reservations, users, and workspaces. Implementations of this
 * interface will handle the actual sending of notifications (e.g., via email,
 * SMS, etc.).
 *
 * The three main notification methods are:
 * 1. Reservation Confirmation: To notify the user of a confirmed reservation.
 * 2. User Confirmation: To notify the user of successful registration or changes.
 * 3. Workspace Confirmation: To notify the user or admin of workspace availability.
 */
public interface NotificationService {

    void sendReservationConfirmation(Reservation reservation);
    void sendUserConfirmation(User user);
    void sendWorkspaceConfirmation(Workspace workspace);
}
