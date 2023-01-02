package com.officemanagementsystemapi.controller;

import com.officemanagementsystemapi.json.PeopleQuery;
import com.officemanagementsystemapi.json.PeopleResponse;
import com.officemanagementsystemapi.service.PeopleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PeopleController {

    private final PeopleService peopleService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("api/peoples")
    public ResponseEntity<List<PeopleResponse>> getAllPeople() {
        return new ResponseEntity<>(peopleService.getAll(), HttpStatus.OK);
    }

    @PostMapping("api/peoples/search")
    public ResponseEntity<List<PeopleResponse>> getAllPeopleByQuery(@RequestBody PeopleQuery peopleQuery) {
        logger.info("People Query is {}", peopleQuery);
        return new ResponseEntity<>(peopleService.getAllByQuery(peopleQuery), HttpStatus.OK);
    }

    @GetMapping("api/peoples/{id}")
    public ResponseEntity<PeopleResponse> getOnePeople(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(peopleService.getOnePeople(id), HttpStatus.OK);
    }

    @PostMapping("api/peoples")
    public ResponseEntity<PeopleResponse> saveOne(@RequestBody @Valid PeopleResponse peopleResponse) {
        return new ResponseEntity<>(peopleService.saveOne(peopleResponse), HttpStatus.CREATED);
    }

    @PutMapping("api/peoples/{id}")
    public ResponseEntity<PeopleResponse> updateOne(
            @PathVariable("id") Integer id,
            @RequestBody @Valid PeopleResponse peopleResponse) {
        return new ResponseEntity<>(peopleService.editOne(peopleResponse, id), HttpStatus.OK);
    }

    @DeleteMapping("api/peoples/{id}")
    public ResponseEntity<String> deleteOne(@PathVariable("id") Integer id) {
        peopleService.deleteOne(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
