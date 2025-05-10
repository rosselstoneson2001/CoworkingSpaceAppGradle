package com.example.repositories;

import java.util.Collection;
import java.util.List;

public interface DataStorage<E> {

    void save(Collection<E> data);

    List<E> load();
}
