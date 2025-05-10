package com.example.repositories.impl;

import com.example.entities.Reservation;
import com.example.exceptions.ReservationNotFoundException;
import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.DataStorage;
import com.example.repositories.ReservationRepository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final TreeSet<Reservation> reservations;
    private DataStorage<Reservation> dataStorage;

    public ReservationRepositoryImpl() {
        this.reservations = new TreeSet<>(Comparator.comparing(Reservation::getReservationCreatedAt)); // Sorted by date
    }

    @Override
    public void add(Reservation reservation) {
        reservation.setReservationId(generateId());
        reservations.add(reservation);
    }

    @Override
    public List<Reservation> getReservationsByCustomer(String customerName) {
        List<Reservation> customerReservation = reservations.stream()
                .filter(r -> r.getCustomerName().equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
        if (customerReservation.isEmpty()) {
            throw new ReservationNotFoundException(ErrorCodes.RESERVATION_NOT_FOUND, "No reservation found with customer name: " + customerName);
        }
        return customerReservation;
    }

    @Override
    public List<Reservation> getAll() {
        return new ArrayList<>(reservations);
    }

    @Override
    public Optional<Reservation> getById(Long reservationId) {
        return reservations.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst();
    }

    @Override
    public List<Reservation> findReservationsByWorkspace(Long workspaceId) {
        return reservations.stream()
                .filter(r -> r.getWorkspaceId().equals(workspaceId))
                .collect(Collectors.toList());

    }

    @Override
    public void remove(Long reservationId) {
        reservations.removeIf(r -> r.getReservationId().equals(reservationId));
    }

    @Override
    public void save() { // Saving data to JSON files
        dataStorage.save(reservations);
    }

    @Override
    public void load() { // Load data from JSON
        // Initialize dataStorage
        this.dataStorage = new DataStorageImpl<>(
                "reservations.json",  // File where reservations are stored
                new TypeReference<>() {
                }
        );

        reservations.addAll(dataStorage.load());
    }


}
