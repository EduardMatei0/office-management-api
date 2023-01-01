package com.officemanagementsystemapi.repository;

import com.officemanagementsystemapi.model.Category;
import com.officemanagementsystemapi.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findFirstByName(String name);
    List<Category> findAllByDepartment(Department department);
    Set<Category> findAllByNameIn(List<String> names);
}
