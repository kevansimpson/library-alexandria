package com.aravo.library.service;

import com.aravo.library.data.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class LibraryService implements AuthorService {
    @Autowired
    private AuthorService authorService;

    public List<Author> fetchAuthors() {
        return authorService.fetchAuthors();
    }

    public Author createAuthor(Author author) {
        return authorService.createAuthor(author);
    }

    public Author updateAuthor(Author author) {
        return authorService.updateAuthor(author);
    }

    public Author deleteAuthor(Author author) {
        // TODO check for existing works
        return authorService.deleteAuthor(author);
    }
}
