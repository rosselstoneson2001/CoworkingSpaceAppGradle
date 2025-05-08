package com.example.services.notifications.listeners;

import com.example.services.notifications.NotificationService;
import com.example.services.notifications.events.ReservationConfirmationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener class for handling reservation creation events and triggering
 * corresponding notification actions.
 *
 * This class listens to events related to reservation creation (such as the
 * {@link ReservationConfirmationEvent}) and uses the {@link NotificationService}
 * to send a reservation confirmation to the appropriate recipient.
 *
 * The listener is triggered by events of type {@link ReservationConfirmationEvent}
 * and sends a confirmation notification via the NotificationService.
 */
@Component
public class ReservationNotificationListener {

    private final NotificationService notificationService;

    public ReservationNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Event listener method that is triggered when a {@link ReservationConfirmationEvent}
     * is published. This method sends a reservation confirmation notification.
     *
     * @param event the ReservationConfirmationEvent that contains the reservation details
     */
    @EventListener
    public void onReservationCreated(ReservationConfirmationEvent event) {
        notificationService.sendReservationConfirmation(event.getReservation());
    }
}
