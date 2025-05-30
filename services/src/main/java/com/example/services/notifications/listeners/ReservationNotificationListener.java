package com.example.services.notifications.listeners;

import com.example.services.notifications.NotificationService;
import com.example.services.notifications.events.ReservationConfirmationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationNotificationListener {

    private final NotificationService notificationService;

    public ReservationNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void onReservationCreated(ReservationConfirmationEvent event) {
        notificationService.sendReservationConfirmation(event.getReservation());
    }
}
