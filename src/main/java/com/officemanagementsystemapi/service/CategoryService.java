package com.officemanagementsystemapi.service;


import com.officemanagementsystemapi.json.response.CategoryResponse;
import com.officemanagementsystemapi.model.Category;
import com.officemanagementsystemapi.model.Department;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    List<Category> saveOrGetByNames(Set<CategoryResponse> categoryResponseList, Department department);
    List<Category> editCategories(Set<CategoryResponse> categoryResponseList, Department department);
}
