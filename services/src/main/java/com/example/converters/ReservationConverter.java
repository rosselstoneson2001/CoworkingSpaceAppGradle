package com.example.converters;

import com.example.dto.requests.ReservationRequestDTO;
import com.example.dto.responses.ReservationResponseDTO;
import com.example.entities.Reservation;
import com.example.entities.Workspace;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationConverter {

    public static Reservation toEntity(ReservationRequestDTO dto, Workspace workspace) {
            Reservation reservation = new Reservation();

            reservation.setWorkspace(workspace);
            reservation.setCustomerName(dto.getCustomerName());
            reservation.setStartDateTime(dto.getStartDateTime());
            reservation.setEndDateTime(dto.getEndDateTime());

            return reservation;
        }

    public static ReservationResponseDTO toDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();

        dto.setReservationId(reservation.getReservationId());
        dto.setCustomerName(reservation.getCustomerName());
        dto.setStartDateTime(reservation.getStartDateTime());
        dto.setEndDateTime(reservation.getEndDateTime());

        return dto;
    }

    public static List<ReservationResponseDTO> toDTO(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationConverter::toDTO)
                .collect(Collectors.toList());
    }
}
