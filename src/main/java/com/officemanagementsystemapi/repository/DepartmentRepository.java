package com.officemanagementsystemapi.repository;

import com.officemanagementsystemapi.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department findFirstByName(String name);
    Set<Department> findAllByNameIn(List<String> names);
}
