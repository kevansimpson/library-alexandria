package com.aravo.library.controller;

import com.aravo.library.data.Author;
import com.aravo.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/util")
public class UtilityController {
    @Autowired
    private LibraryService libraryService;

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
