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

    void save(T entity);
    List<T> findAll();
    Optional<T> findById(K id);
    void deleteById(K id);

}
