package com.officemanagementsystemapi.controller;

import com.officemanagementsystemapi.json.PeopleResponse;
import com.officemanagementsystemapi.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("api/people")
    public ResponseEntity<List<PeopleResponse>> getAllPeople() {
        return new ResponseEntity<>(peopleService.getAll(), HttpStatus.OK);
    }

    @GetMapping("api/people/{id}")
    public ResponseEntity<PeopleResponse> getOnePeople(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(peopleService.getOnePeople(id), HttpStatus.OK);
    }

    @PostMapping("api/people")
    public ResponseEntity<PeopleResponse> saveOne(@RequestBody @Valid PeopleResponse peopleResponse) {
        return new ResponseEntity<>(peopleService.saveOne(peopleResponse), HttpStatus.CREATED);
    }

    @PutMapping("api/people/{id}")
    public ResponseEntity<PeopleResponse> updateOne(
            @PathVariable("id") Integer id,
            @RequestBody @Valid PeopleResponse peopleResponse) {
        return new ResponseEntity<>(peopleService.editOne(peopleResponse, id), HttpStatus.OK);
    }

    @DeleteMapping("api/people/{id}")
    public ResponseEntity<String> deleteOne(@PathVariable("id") Integer id) {
        peopleService.deleteOne(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
