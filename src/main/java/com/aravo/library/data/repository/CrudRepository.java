package com.aravo.library.data.repository;

import java.util.List;

public interface CrudRepository<T> {
    List<T> findAll();
    T findById(long id);
    T save(T entity);
    void delete(T entity);
}
