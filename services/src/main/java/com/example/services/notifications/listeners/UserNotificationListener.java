package com.example.services.notifications.listeners;

import com.example.services.notifications.NotificationService;
import com.example.services.notifications.events.UserConfirmationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserNotificationListener {

    private final NotificationService notificationService;

    public UserNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void onUserCreated(UserConfirmationEvent event) {
        notificationService.sendUserConfirmation(event.getUser());
    }
}
