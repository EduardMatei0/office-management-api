package com.officemanagementsystemapi.controller;

import com.officemanagementsystemapi.json.response.DepartmentResponse;
import com.officemanagementsystemapi.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("departments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return new ResponseEntity<>(departmentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("departments/{id}")
    public ResponseEntity<DepartmentResponse> getOneDepartment(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(departmentService.getOne(id), HttpStatus.OK);
    }

    @PostMapping("departments")
    public ResponseEntity<DepartmentResponse> saveOneDepartment(@RequestBody DepartmentResponse departmentResponse) {
        return new ResponseEntity<>(departmentService.saveOne(departmentResponse), HttpStatus.CREATED);
    }

    @PutMapping("departments/{id}")
    public ResponseEntity<DepartmentResponse> updateOneDepartment(@PathVariable("id") Integer id,
                                                                  @RequestBody DepartmentResponse departmentResponse) {
        return new ResponseEntity<>(departmentService.editOne(departmentResponse, id), HttpStatus.OK);
    }

    @DeleteMapping("departments/{id}")
    public ResponseEntity<String> deleteOneDepartment(@PathVariable("id") Integer id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
