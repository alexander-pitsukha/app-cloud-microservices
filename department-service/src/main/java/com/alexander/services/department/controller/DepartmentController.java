package com.alexander.services.department.controller;

import com.alexander.services.department.client.EmployeeClient;
import com.alexander.services.department.model.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.alexander.services.department.repository.DepartmentRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentRepository repository;
    private final EmployeeClient employeeClient;

    @PostMapping
    public Department add(@RequestBody Department department) {
        log.info("Department add: {}", department);
        return repository.add(department);
    }

    @GetMapping("{id}")
    public Department findById(@PathVariable("id") Long id) {
        log.info("Department find: id={}", id);
        return repository.findById(id);
    }

    @GetMapping
    public List<Department> findAll() {
        log.info("Department find");
        return repository.findAll();
    }

    @GetMapping("organization/{organizationId}")
    public List<Department> findByOrganization(@PathVariable("organizationId") Long organizationId) {
        log.info("Department find: organizationId={}", organizationId);
        return repository.findByOrganization(organizationId);
    }

    @GetMapping("organization/{organizationId}/with-employees")
    public List<Department> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId) {
        log.info("Department find: organizationId={}", organizationId);
        List<Department> departments = repository.findByOrganization(organizationId);
        departments.forEach(d -> d.setEmployees(employeeClient.findByDepartment(d.getId())));
        return departments;
    }

}
