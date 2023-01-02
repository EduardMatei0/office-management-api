package com.officemanagementsystemapi.service.impl;

import com.officemanagementsystemapi.json.CategoryResponse;
import com.officemanagementsystemapi.model.Category;
import com.officemanagementsystemapi.model.Department;
import com.officemanagementsystemapi.repository.CategoryRepository;
import com.officemanagementsystemapi.repository.PeopleRepository;
import com.officemanagementsystemapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               PeopleRepository peopleRepository) {
        this.categoryRepository = categoryRepository;
        this.peopleRepository = peopleRepository;
    }

    @Override
    public List<Category> saveOrGetByNames(Set<CategoryResponse> categoryResponseList, Department department) {
        return categoryResponseList
                .stream()
                .map(categoryResponse -> saveOrUpdate(categoryResponse, department))
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> editCategories(Set<CategoryResponse> categoryResponseList, Department department) {

        List<Category> newCategories = categoryResponseList
                .stream()
                .map(categoryResponse -> saveOrUpdate(categoryResponse, department))
                .collect(Collectors.toList());

        // remove old categories
        removeOldCategories(newCategories, department);

        return newCategories;
    }

    private void removeOldCategories(List<Category> newCategories, Department department) {
        List<Category> oldCategories = categoryRepository.findAllByDepartment(department);
        oldCategories.removeAll(newCategories);
        categoryRepository.deleteAll(oldCategories);
    }

    private Category saveOrUpdate(CategoryResponse categoryResponse, Department department) {
        Category category = categoryRepository.findFirstByName(categoryResponse.getName())
                .orElseGet(() -> categoryRepository.save(new Category(categoryResponse.getName(), department)));
        if (!category.getDepartment().getId().equals(department.getId())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Ministry with name " + category.getName() + " already exist in " + category.getDepartment().getName());
        category.setPeopleSet(peopleRepository.findAllByIdIn(categoryResponse.getPeopleIds()));
        return categoryRepository.save(category);
    }


}
