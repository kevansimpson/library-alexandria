package com.aravo.library.data.repository;

import com.aravo.library.data.Author;
import com.aravo.library.data.entity.AuthorEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {

}
