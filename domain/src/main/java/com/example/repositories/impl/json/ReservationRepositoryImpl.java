package com.example.repositories.impl.json;

import com.example.entities.Reservation;
import com.example.exceptions.ReservationNotFoundException;
import com.example.exceptions.enums.NotFoundErrorCodes;
import com.example.repositories.DataStorage;
import com.example.repositories.ReservationRepository;
import com.example.repositories.StorageManagerRepository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ReservationRepository} that provides data storage and retrieval for reservations.
 * It uses a {@link TreeSet} to store reservations sorted by their creation date and saves data to and loads data from a JSON file.
 * Implements {@link StorageManagerRepository} to handle data persistence operations.
 */
public class ReservationRepositoryImpl implements ReservationRepository, StorageManagerRepository {

    private final TreeSet<Reservation> reservations;
    private DataStorage<Reservation> dataStorage;

    public ReservationRepositoryImpl() {
        this.reservations = new TreeSet<>(Comparator.comparing(Reservation::getReservationCreatedAt)); // Sorted by date
    }

    /**
     * Adds a reservation to the repository. The reservation ID is generated automatically.
     *
     * @param reservation The reservation to be added to the repository.
     */
    @Override
    public void add(Reservation reservation) {
        reservation.setReservationId(generateId());
        reservations.add(reservation);
    }

    /**
     * Retrieves all reservations for a specific customer by name.
     * Throws a {@link ReservationNotFoundException} if no reservations are found for the given customer.
     *
     * @param customerName The name of the customer whose reservations are to be retrieved.
     * @return A list of reservations for the specified customer.
     * @throws ReservationNotFoundException if no reservations are found for the customer.
     */
    @Override
    public List<Reservation> getReservationsByCustomer(String customerName) {
        List<Reservation> customerReservation = reservations.stream()
                .filter(r -> r.getCustomerName().equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
        if (customerReservation.isEmpty()) {
            throw new ReservationNotFoundException(NotFoundErrorCodes.RESERVATION_NOT_FOUND, "Failed to retrieve reservation by customer. No reservation found with customer name: " + customerName);
        }
        return customerReservation;
    }

    /**
     * Retrieves all reservations from the repository.
     *
     * @return A list of all reservations stored in the repository.
     */
    @Override
    public List<Reservation> getAll() {
        return new ArrayList<>(reservations);
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param reservationId The ID of the reservation to be retrieved.
     * @return An {@link Optional} containing the reservation if found, or empty if not found.
     */
    @Override
    public Optional<Reservation> getById(Long reservationId) {
        return reservations.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst();
    }

    /**
     * Retrieves all reservations for a specific workspace.
     *
     * @param workspaceId The ID of the workspace for which reservations are to be retrieved.
     * @return A list of reservations associated with the specified workspace.
     */
    @Override
    public List<Reservation> findReservationsByWorkspace(Long workspaceId) {
        return reservations.stream()
                .filter(r -> r.getWorkspace().equals(workspaceId))
                .collect(Collectors.toList());

    }

    /**
     * Removes a reservation by its ID.
     *
     * @param reservationId The ID of the reservation to be removed.
     */
    @Override
    public void remove(Long reservationId) {
        reservations.removeIf(r -> r.getReservationId().equals(reservationId));
    }

    /**
     * Saves the current state of the reservations to a JSON file.
     * The reservations are serialized and written to the file defined in the data storage.
     */
    @Override
    public void save() { // Saving data to JSON files
        dataStorage.save(reservations);
    }

    /**
     * Loads the reservations from a JSON file into the repository.
     * Initializes the {@link DataStorage} and reads the data from the file.
     */
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
