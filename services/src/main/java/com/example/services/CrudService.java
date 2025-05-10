package com.example.services;

import com.example.repositories.StorageManagerRepository;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, K> extends StorageManagerRepository {

    void create(T entity);
    List<T> getAll();
    Optional<T> getById(K id);
    void remove(K id);

}
