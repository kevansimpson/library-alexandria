package com.aravo.library.data.repository;

import com.aravo.library.data.Persistable;

import java.util.List;

public interface CrudRepository<T extends Persistable> {
    T create(T entity);
    List<T> findAll();
    T findById(long id);
    T update(T entity);
    boolean delete(long id);

    default T save(T entity) {
        return (entity.getId() == 0) ? create(entity) : update(entity);
    }
}
