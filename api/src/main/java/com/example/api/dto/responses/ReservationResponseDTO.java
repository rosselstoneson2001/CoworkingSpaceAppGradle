package com.example.api.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO {

    private Long reservationId;
    private String customerName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}
