package com.aravo.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/util")
public class UtilityController {
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<String>> findAll(){
//        List<Product> allProducts = productService.findAll();
        return new ResponseEntity<>(List.of("foo", "bar"), HttpStatus.OK);
    }

}
