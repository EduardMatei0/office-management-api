package com.officemanagementsystemapi.service.impl;

import com.officemanagementsystemapi.json.DepartmentResponse;
import com.officemanagementsystemapi.model.Department;
import com.officemanagementsystemapi.repository.DepartmentRepository;
import com.officemanagementsystemapi.repository.PeopleRepository;
import com.officemanagementsystemapi.service.CategoryService;
import com.officemanagementsystemapi.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CategoryService categoryService;
    private final PeopleRepository peopleRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 CategoryService categoryService,
                                 PeopleRepository peopleRepository) {
        this.departmentRepository = departmentRepository;
        this.categoryService = categoryService;
        this.peopleRepository = peopleRepository;
    }

    @Override
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse getOne(Integer id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        return new DepartmentResponse(department);
    }

    @Override
    public DepartmentResponse saveOne(DepartmentResponse departmentResponse) {

        // get department by name or save new one
        Department department = departmentRepository.findFirstByName(departmentResponse.getName());
        if (Objects.nonNull(department)) throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Department already exists");
        department = departmentRepository.save(new Department(departmentResponse.getName()));

        // set relationships
        department.setCategoryList(categoryService.saveOrGetByNames(departmentResponse.getCategories(), department));
        department.setPeoples(peopleRepository.findAllByIdIn(departmentResponse.getCategories()
                .stream()
                .flatMap(categoryResponse ->  categoryResponse.getPeopleIds().stream())
                .collect(Collectors.toList())));
        department.setLeaders(peopleRepository.findAllByIdIn(departmentResponse.getLeadersIds()));
        departmentRepository.save(department);

        return new DepartmentResponse(department);
    }

    @Override
    public DepartmentResponse editOne(DepartmentResponse departmentResponse, Integer id) {

        // get department by id and edit
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        department.setName(departmentResponse.getName());

        // set relationship
        department.setCategoryList(categoryService.editCategories(departmentResponse.getCategories(), department));
        department.setPeoples(peopleRepository.findAllByIdIn(departmentResponse.getCategories()
                .stream()
                .flatMap(categoryResponse ->  categoryResponse.getPeopleIds().stream())
                .collect(Collectors.toList())));
        department.setLeaders(peopleRepository.findAllByIdIn(departmentResponse.getLeadersIds()));
        departmentRepository.save(department);

        return new DepartmentResponse(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(Integer id) {
        departmentRepository.deleteById(id);
    }
}
