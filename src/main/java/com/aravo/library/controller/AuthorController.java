package com.aravo.library.controller;

import com.aravo.library.data.entity.Author;
import com.aravo.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService service) {
        authorService = service;
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.createAuthor(author), HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Author>> getAuthors() {
        return new ResponseEntity<>(authorService.fetchAuthors(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.updateAuthor(author), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Author> deleteAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.deleteAuthor(author), HttpStatus.NO_CONTENT);
    }
}
