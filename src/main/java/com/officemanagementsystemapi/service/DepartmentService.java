package com.officemanagementsystemapi.service;


import com.officemanagementsystemapi.json.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    List<DepartmentResponse> getAll();

    DepartmentResponse getOne(Integer id);
    DepartmentResponse saveOne(DepartmentResponse departmentResponse);
    DepartmentResponse editOne(DepartmentResponse departmentResponse, Integer id);
    void deleteDepartment(Integer id);
}
