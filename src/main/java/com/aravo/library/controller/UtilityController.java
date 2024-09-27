package com.aravo.library.controller;

import com.aravo.library.data.entity.Author;
import com.aravo.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/util")
public class UtilityController {
    @Autowired
    private LibraryService libraryService;

    @PostMapping(path = "/author") // not working
    public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
        System.out.println("\n\n"+ author +"\n\n");
        return new ResponseEntity<>(libraryService.saveAuthor(author), HttpStatus.CREATED);
    }
    @GetMapping(path = "/author", produces = "application/json")
    public ResponseEntity<List<Author>> getAuthors() {
        return new ResponseEntity<>(libraryService.getAuthors(), HttpStatus.OK);
    }

    @GetMapping(path = "/test", produces = "application/json")
    public ResponseEntity<List<String>> findAll(){
//        List<Product> allProducts = productService.findAll();
        return new ResponseEntity<>(List.of("foo", "bar"), HttpStatus.OK);
    }

}
