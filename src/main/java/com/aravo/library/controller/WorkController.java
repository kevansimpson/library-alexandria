package com.aravo.library.controller;

import com.aravo.library.data.entity.Work;
import com.aravo.library.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work")
public class WorkController {

    private final WorkService workService;

    @Autowired
    public WorkController(WorkService service) {
        workService = service;
    }

    @PostMapping
    public ResponseEntity<Work> createWork(@RequestBody Work work) {
        return new ResponseEntity<>(workService.createWork(work), HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Work>> getWorks() {
        return new ResponseEntity<>(workService.fetchWorks(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Work> updateWork(@RequestBody Work work) {
        return new ResponseEntity<>(workService.updateWork(work), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Work> deleteWork(@RequestBody Work work) {
        return new ResponseEntity<>(workService.deleteWork(work), HttpStatus.NO_CONTENT);
    }
}
