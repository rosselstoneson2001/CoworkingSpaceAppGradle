package com.example.services.notifications.events;

import com.example.domain.entities.Reservation;

public class ReservationConfirmationEvent {

    private final Reservation reservation;

    public ReservationConfirmationEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
