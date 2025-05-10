package com.example.repositories;

/**
 * Repository interface for managing the storage of data.
 * <p>
 * This interface defines basic methods for saving and loading data to and from a storage medium.
 * Implementations of this interface can handle different types of data storage (e.g., files, databases).
 * </p>
 */
public interface StorageManagerRepository {

    void save();
    void load();

}
