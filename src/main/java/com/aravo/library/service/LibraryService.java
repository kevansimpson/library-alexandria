package com.aravo.library.service;

import com.aravo.library.data.Author;
import com.aravo.library.data.entity.AuthorEntity;
import com.aravo.library.data.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {
    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        for (AuthorEntity a : authorRepository.findAll())
            authors.add(new Author(a.getId(), a.getName()));
        return authors;
    }
}
