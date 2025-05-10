package com.example.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Generic repository interface for basic CRUD operations.
 * <p>
 * This interface provides the foundational methods for working with entities in a repository.
 * It includes operations for adding, retrieving, updating, and removing entities of type {@code T}.
 * Additionally, it provides a default method for generating a unique identifier for the entities.
 * </p>
 *
 * @param <T> the type of entity that the repository manages
 * @param <K> the type of the entity's identifier
 */
public interface CrudRepository<T, K> {

    void add(T entity);
    List<T> getAll();
    Optional<T> getById(K id);
    void remove(K id);

    default Long generateId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

}
