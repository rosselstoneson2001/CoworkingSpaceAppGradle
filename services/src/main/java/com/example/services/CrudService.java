package com.example.services;

import java.util.List;
import java.util.Optional;

/**
 * A generic service interface that provides CRUD (Create, Read, Update, Delete) operations
 * for entities in the application.
 *
 * @param <T> the type of the entity
 * @param <K> the type of the identifier of the entity
 */
public interface CrudService<T, K>  {

    void create(T entity);
    List<T> getAll();
    Optional<T> getById(K id);
    void remove(K id);

}
