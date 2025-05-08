package com.example.services.notifications.events;

import com.example.domain.entities.Reservation;

/**
 * Event class that represents a reservation confirmation event.
 *
 * This event is used to notify listeners when a reservation has been made and
 * a confirmation needs to be sent. The event encapsulates the {@link Reservation}
 * entity, which contains all the details necessary for the confirmation process.
 *
 * Listeners can subscribe to this event to perform actions like sending a confirmation
 * email or logging the details of the reservation.
 */
public class ReservationConfirmationEvent {

    private final Reservation reservation;

    public ReservationConfirmationEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
