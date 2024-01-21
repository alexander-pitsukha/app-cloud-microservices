package com.alexander.services.organization.controller;

import java.util.List;

import com.alexander.services.organization.client.DepartmentClient;
import com.alexander.services.organization.client.EmployeeClient;
import com.alexander.services.organization.model.Organization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alexander.services.organization.repository.OrganizationRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {

    private final OrganizationRepository repository;
    private final DepartmentClient departmentClient;
    private final EmployeeClient employeeClient;

    @PostMapping
    public Organization add(@RequestBody Organization organization) {
        log.info("Organization add: {}", organization);
        return repository.add(organization);
    }

    @GetMapping
    public List<Organization> findAll() {
        log.info("Organization find");
        return repository.findAll();
    }

    @GetMapping("{id}")
    public Organization findById(@PathVariable("id") Long id) {
        log.info("Organization find (findById): id={}", id);
        return repository.findById(id);
    }

    @GetMapping("{id}/with-departments")
    public Organization findByIdWithDepartments(@PathVariable("id") Long id) {
        log.info("Organization find (findByIdWithDepartments): id={}", id);
        Organization organization = repository.findById(id);
        organization.setDepartments(departmentClient.findByOrganization(organization.getId()));
        return organization;
    }

    @GetMapping("{id}/with-departments-and-employees")
    public Organization findByIdWithDepartmentsAndEmployees(@PathVariable("id") Long id) {
        log.info("Organization find: id={}", id);
        Organization organization = repository.findById(id);
        organization.setDepartments(departmentClient.findByOrganizationWithEmployees(organization.getId()));
        return organization;
    }

    @GetMapping("{id}/with-employees")
    public Organization findByIdWithEmployees(@PathVariable("id") Long id) {
        log.info("Organization find (findByIdWithEmployees): id={}", id);
        Organization organization = repository.findById(id);
        organization.setEmployees(employeeClient.findByOrganization(organization.getId()));
        return organization;
    }

}
