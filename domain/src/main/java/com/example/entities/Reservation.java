package com.example.entities;

import java.time.LocalDateTime;

public class Reservation {

    private Long reservationId;
    private Long workspaceId;
    private String customerName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime reservationCreatedAt;

    public Reservation(Long workspaceId,
                       String customerName,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime,
                       LocalDateTime reservationCreatedAt) {
        this.workspaceId = workspaceId;
        this.customerName = customerName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reservationCreatedAt = reservationCreatedAt;
    }

    public Reservation(Long reservationId,
                       Long workspaceId,
                       String customerName,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime,
                       LocalDateTime reservationCreatedAt) {
        this.reservationId = reservationId;
        this.workspaceId = workspaceId;
        this.customerName = customerName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reservationCreatedAt = reservationCreatedAt;
    }

    public Reservation() {} // For JSON Deserialization

    public Long getReservationId() { return reservationId; }
    public Long getWorkspaceId() { return workspaceId; }
    public String getCustomerName() { return customerName; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public LocalDateTime getReservationCreatedAt() { return reservationCreatedAt; }

    public void setWorkspaceId(Long workspaceId) { this.workspaceId = workspaceId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
    public void setReservationCreatedAt(LocalDateTime reservationCreatedAt) { this.reservationCreatedAt = reservationCreatedAt; }

    @Override
    public String toString() {
        return "\n========================================" +
                "\nReservation ID: " + reservationId +
                "\nWorkspace ID: " + workspaceId +
                "\nCustomer: " + customerName +
                "\nTime: " + startDateTime + " to " + endDateTime +
                "\n=======================================";
    }
}
