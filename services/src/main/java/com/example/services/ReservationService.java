package com.example.services;


import com.example.entities.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService extends CrudService<Reservation, Long> {

    List<Reservation> findReservationsByCustomer(String customerName);

    List<Reservation> findReservationsByWorkspace(Long workspaceId);

    boolean isWorkspaceAvailable(Long workspaceId, LocalDateTime startDateTime, LocalDateTime endDateTime);


}
