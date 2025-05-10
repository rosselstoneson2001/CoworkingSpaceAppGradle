package com.example.repositories;

import com.example.entities.Reservation;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long>, StorageManagerRepository {

    List<Reservation> getReservationsByCustomer(String customerName);

    List<Reservation> findReservationsByWorkspace(Long workspaceId);

}
