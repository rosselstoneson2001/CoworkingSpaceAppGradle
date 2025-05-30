package com.example.api.converters;

import com.example.api.dto.requests.ReservationRequestDTO;
import com.example.api.dto.responses.ReservationResponseDTO;
import com.example.domain.entities.Reservation;
import com.example.domain.entities.User;
import com.example.domain.entities.Workspace;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationConverter {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static Reservation toEntity(ReservationRequestDTO dto, Workspace workspace, User customer) {
        Reservation reservation = modelMapper.map(dto, Reservation.class);
        reservation.setWorkspace(workspace);
        reservation.setCustomer(customer);
        return reservation;
    }

    public static ReservationResponseDTO toDTO(Reservation reservation) {
        return modelMapper.map(reservation, ReservationResponseDTO.class);
    }

    public static List<ReservationResponseDTO> toDTO(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationConverter::toDTO)
                .collect(Collectors.toList());
    }
}
