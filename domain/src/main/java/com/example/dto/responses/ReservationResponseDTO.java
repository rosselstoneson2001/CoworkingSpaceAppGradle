package com.example.dto.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {

    private Long reservationId;
    private String customerName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}
