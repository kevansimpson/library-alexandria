package com.aravo.library.controller;

import com.aravo.library.data.entity.Author;
import com.aravo.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {
    @Autowired
    private LibraryService libraryService;

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(libraryService.createAuthor(author), HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Author>> getAuthors() {
        return new ResponseEntity<>(libraryService.fetchAuthors(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(libraryService.updateAuthor(author), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Author> deleteAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(libraryService.deleteAuthor(author), HttpStatus.OK);
    }
}
