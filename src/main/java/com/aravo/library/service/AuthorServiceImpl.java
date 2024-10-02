package com.aravo.library.service;

import com.aravo.library.data.entity.Author;
import com.aravo.library.data.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository repository) {
        authorRepository = repository;
    }

    public List<Author> fetchAuthors() {
        return authorRepository.findAll();
    }

    public Author findAuthorById(long id) {
        return authorRepository.findById(id);
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author updateAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author deleteAuthor(Author author) {
        // TODO check for existing works
        authorRepository.delete(author);
        return findAuthorById(author.getId());
    }
}
