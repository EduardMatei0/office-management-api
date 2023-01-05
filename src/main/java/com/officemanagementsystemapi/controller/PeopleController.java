package com.officemanagementsystemapi.controller;

import com.officemanagementsystemapi.json.request.PeopleQuery;
import com.officemanagementsystemapi.json.response.PeopleResponse;
import com.officemanagementsystemapi.service.PeopleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class PeopleController {

    private final PeopleService peopleService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("peoples")
    public ResponseEntity<List<PeopleResponse>> getAllPeople(Principal principal) {
        logger.info("Principal is {}", principal);
        return new ResponseEntity<>(peopleService.getAll(), HttpStatus.OK);
    }

    @PostMapping("peoples/search")
    public ResponseEntity<List<PeopleResponse>> getAllPeopleByQuery(@RequestBody PeopleQuery peopleQuery) {
        logger.info("People Query is {}", peopleQuery);
        return new ResponseEntity<>(peopleService.getAllByQuery(peopleQuery), HttpStatus.OK);
    }

    @GetMapping("peoples/{id}")
    public ResponseEntity<PeopleResponse> getOnePeople(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(peopleService.getOnePeople(id), HttpStatus.OK);
    }

    @PostMapping("peoples")
    public ResponseEntity<PeopleResponse> saveOne(@RequestBody @Valid PeopleResponse peopleResponse) {
        return new ResponseEntity<>(peopleService.saveOne(peopleResponse), HttpStatus.CREATED);
    }

    @PutMapping("peoples/{id}")
    public ResponseEntity<PeopleResponse> updateOne(
            @PathVariable("id") Integer id,
            @RequestBody @Valid PeopleResponse peopleResponse) {
        return new ResponseEntity<>(peopleService.editOne(peopleResponse, id), HttpStatus.OK);
    }

    @DeleteMapping("peoples/{id}")
    public ResponseEntity<String> deleteOne(@PathVariable("id") Integer id) {
        peopleService.deleteOne(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
