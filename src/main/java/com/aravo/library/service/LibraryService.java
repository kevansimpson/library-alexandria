package com.aravo.library.service;

import com.aravo.library.data.entity.Author;
import com.aravo.library.data.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public LibraryService(AuthorRepository repository) {
        authorRepository = repository;
    }

    public List<Author> fetchAuthors() {
        List<Author> authors = new ArrayList<>();
        for (Author a : authorRepository.findAll())
            authors.add(a);
        return authors;
    }

    public Author findAuthorById(long id) {
        return authorRepository.findById(id).orElse(null);
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
