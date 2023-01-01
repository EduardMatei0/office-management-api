package com.officemanagementsystemapi.service.impl;

import com.officemanagementsystemapi.json.PeopleResponse;
import com.officemanagementsystemapi.model.People;
import com.officemanagementsystemapi.repository.CategoryRepository;
import com.officemanagementsystemapi.repository.DepartmentRepository;
import com.officemanagementsystemapi.repository.PeopleRepository;
import com.officemanagementsystemapi.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PeopleServiceImpl implements PeopleService {

    private final PeopleRepository peopleRepository;
    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PeopleServiceImpl(PeopleRepository peopleRepository,
                             DepartmentRepository departmentRepository,
                             CategoryRepository categoryRepository) {
        this.peopleRepository = peopleRepository;
        this.departmentRepository = departmentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PeopleResponse getOnePeople(Integer id) {
        People people = peopleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "People not found"));
        return new PeopleResponse(people);
    }

    @Override
    public List<PeopleResponse> getAll() {
        return peopleRepository.findAll()
                .stream()
                .map(PeopleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public PeopleResponse saveOne(PeopleResponse peopleResponse) {
        Optional<People> peopleOptional = peopleRepository.findFirstByEmail(peopleResponse.getEmail());
        if (peopleOptional.isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");

        People people = new People(peopleResponse);
        // add departments and categories to people
        people.setDepartments(departmentRepository.findAllByNameIn(peopleResponse.getDepartments()));
        people.setCategories(categoryRepository.findAllByNameIn(peopleResponse.getCategories()));

        return new PeopleResponse(peopleRepository.save(people));
    }

    @Override
    public PeopleResponse editOne(PeopleResponse peopleResponse, Integer id) {
        People people = peopleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "People not found"));
        people.setName(peopleResponse.getName());
        people.setEmail(peopleResponse.getEmail());
        people.setPhoneNumber(peopleResponse.getPhoneNumber());
        // add departments and categories to people
        people.setDepartments(departmentRepository.findAllByNameIn(peopleResponse.getDepartments()));
        people.setCategories(categoryRepository.findAllByNameIn(peopleResponse.getCategories()));
        peopleRepository.save(people);
        return new PeopleResponse(people);
    }

    @Override
    public void deleteOne(Integer id) {
        peopleRepository.deleteById(id);
    }
}
