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

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Work> findWork(@PathVariable long id) {
        Work work = workService.findWorkById(id);
        if (work == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(work, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Work>> getWorks() {
        return new ResponseEntity<>(workService.fetchWorks(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Work> updateWork(@RequestBody Work work) {
        return new ResponseEntity<>(workService.updateWork(work), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Work> deleteWork(@PathVariable long id) {
        if (workService.deleteWork(id))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
