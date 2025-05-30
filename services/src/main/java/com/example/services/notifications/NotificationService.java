package com.example.services.notifications;

import com.example.domain.entities.Reservation;
import com.example.domain.entities.User;
import com.example.domain.entities.Workspace;

public interface NotificationService {

    void sendReservationConfirmation(Reservation reservation);
    void sendUserConfirmation(User user);
    void sendWorkspaceConfirmation(Workspace workspace);
}
