package com.aravo.library.service;

import com.aravo.library.data.entity.Author;

import java.util.List;

public interface AuthorService {
    Author createAuthor(Author author);
    List<Author> fetchAuthors();
    Author updateAuthor(Author author);
    Author deleteAuthor(Author author);
}
