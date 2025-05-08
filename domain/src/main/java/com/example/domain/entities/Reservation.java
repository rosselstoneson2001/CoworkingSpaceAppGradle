package com.example.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "reservation")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

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

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    @JsonIgnore
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;


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
