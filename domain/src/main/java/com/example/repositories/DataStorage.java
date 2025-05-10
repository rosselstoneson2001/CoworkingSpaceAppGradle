package com.example.repositories;

import java.util.Collection;
import java.util.List;

/**
 * Interface for a data storage system that supports saving and loading entities.
 * <p>
 * This interface defines methods for saving a collection of entities to a data source
 * and loading entities from a data source. It is designed to be implemented by different
 * storage solutions, such as file-based storage, database storage, or in-memory storage.
 * </p>
 *
 * @param <E> The type of entity to be saved and loaded.
 */
public interface DataStorage<E> {

    void save(Collection<E> data);
    List<E> load();

}
