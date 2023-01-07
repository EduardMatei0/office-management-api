package com.officemanagementsystemapi.service.impl;

import com.officemanagementsystemapi.json.request.PeopleQuery;
import com.officemanagementsystemapi.json.response.PeopleResponse;
import com.officemanagementsystemapi.model.Category;
import com.officemanagementsystemapi.model.Department;
import com.officemanagementsystemapi.model.People;
import com.officemanagementsystemapi.repository.CategoryRepository;
import com.officemanagementsystemapi.repository.DepartmentRepository;
import com.officemanagementsystemapi.repository.PeopleRepository;
import com.officemanagementsystemapi.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PeopleServiceImpl implements PeopleService {

    private final PeopleRepository peopleRepository;
    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;
    @PersistenceContext
    private EntityManager entityManager;

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
    @Transactional
    public List<PeopleResponse> getAllByQuery(PeopleQuery peopleQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<People> criteriaQuery = criteriaBuilder.createQuery(People.class);
        Root<People> peopleRoot = criteriaQuery.from(People.class);
        criteriaQuery.select(peopleRoot);

        List<Predicate> predicates = new ArrayList<>();
        if (peopleQuery.getNames().size() > 0) predicates.add(peopleRoot.get("name").in(peopleQuery.getNames()));
        if (peopleQuery.getEmails().size() > 0) predicates.add(peopleRoot.get("email").in(peopleQuery.getEmails()));
        if (peopleQuery.getPhoneNumbers().size() > 0) predicates.add(peopleRoot.get("phoneNumber").in(peopleQuery.getPhoneNumbers()));
        if (peopleQuery.getDepartments().size() > 0) {
            Join<People, Department> departmentJoin = peopleRoot.join("departments");
            predicates.add(departmentJoin.get("name").in(peopleQuery.getDepartments()));
        }
        if (peopleQuery.getCategories().size() > 0) {
            Join<People, Category> categoryJoin = peopleRoot.join("categories");
            predicates.add(categoryJoin.get("name").in(peopleQuery.getCategories()));
        }
        if (predicates.size() > 0) criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

        return entityManager.createQuery(criteriaQuery).getResultList()
                .stream()
                .distinct()
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
