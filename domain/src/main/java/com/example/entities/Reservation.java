package com.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    @JsonIgnore
    private Workspace workspace;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "reservation_created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime reservationCreatedAt;

    @Column(name = "is_active")
    private boolean isActive = true;

    public Reservation(Workspace workspace,
                       String customerName,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime,
                       LocalDateTime reservationCreatedAt) {
        this.workspace = workspace;
        this.customerName = customerName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reservationCreatedAt = reservationCreatedAt;
    }

    public Reservation(Long reservationId,
                       Workspace workspace,
                       String customerName,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime,
                       LocalDateTime reservationCreatedAt) {
        this.reservationId = reservationId;
        this.workspace = workspace;
        this.customerName = customerName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reservationCreatedAt = reservationCreatedAt;
    }

    public Reservation() {} // For JSON Deserialization

    public Long getReservationId() { return reservationId; }
    public Workspace getWorkspace() { return workspace; }
    public String getCustomerName() { return customerName; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public LocalDateTime getReservationCreatedAt() { return reservationCreatedAt; }
    public boolean isActive() { return isActive; }

    public void setWorkspace(Workspace workspaceId) { this.workspace = workspaceId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
    public void setReservationCreatedAt(LocalDateTime reservationCreatedAt) { this.reservationCreatedAt = reservationCreatedAt; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    @Override
    public String toString() {
        return "\n========================================" +
                "\nReservation ID: " + reservationId +
                "\nWorkspace ID: " + workspace +
                "\nCustomer: " + customerName +
                "\nTime: " + startDateTime + " to " + endDateTime +
                "\n=======================================";
    }
}
