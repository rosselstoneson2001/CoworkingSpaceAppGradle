package com.example.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudRepository<T, K> {

    void add(T entity);
    List<T> getAll();
    Optional<T> getById(K id);
    void remove(K id);

    default Long generateId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

}
