package com.example.services.notifications.listeners;

import com.example.services.notifications.NotificationService;
import com.example.services.notifications.events.UserConfirmationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener class for handling user creation events and triggering corresponding
 * notification actions.
 *
 * This class listens to events related to user creation (such as the
 * {@link UserConfirmationEvent}) and uses the {@link NotificationService}
 * to send a user confirmation notification to the appropriate recipient.
 *
 * The listener is triggered by events of type {@link UserConfirmationEvent}
 * and sends a user confirmation notification via the NotificationService.
 */
@Component
public class UserNotificationListener {

    private final NotificationService notificationService;

    public UserNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Event listener method that is triggered when a {@link UserConfirmationEvent}
     * is published. This method sends a user confirmation notification.
     *
     * @param event the UserConfirmationEvent that contains the user details
     */
    @EventListener
    public void onUserCreated(UserConfirmationEvent event) {
        notificationService.sendUserConfirmation(event.getUser());
    }
}
