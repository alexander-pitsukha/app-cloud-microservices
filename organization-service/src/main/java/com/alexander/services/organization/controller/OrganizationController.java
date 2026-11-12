package com.alexander.services.organization.controller;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.DepartmentDto;
import com.alexander.dto.EmployeeDto;
import com.alexander.dto.OrganizationDto;
import com.alexander.services.organization.client.DepartmentClient;
import com.alexander.services.organization.client.EmployeeClient;
import com.alexander.services.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {

    private final OrganizationService organizationService;
    private final DepartmentClient departmentClient;
    private final EmployeeClient employeeClient;

    @GetMapping
    public ResponseEntity<Page<OrganizationDto>> getAll(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        log.info("Organization get");
        return ResponseEntity.ok(organizationService.findAll(page != null ? page : 0, size != null ? size : 0));
    }

    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getAutocomplete(@RequestParam String query, @RequestParam Integer limit) {
        log.info("Organization get (autocomplete).");
        return ResponseEntity.ok(organizationService.findAutocomplete(query, limit));
    }

    @GetMapping("{id}")
    public ResponseEntity<OrganizationDto> getById(@PathVariable Long id) {
        log.info("Organization get: id={}", id);
        return ResponseEntity.ok(organizationService.findById(id));
    }

    @GetMapping("{id}/with-departments")
    public ResponseEntity<OrganizationDto> getByIdWithDepartments(@PathVariable Long id) {
        log.info("Organization get: id={} with departments", id);
        var organizationDto = organizationService.findById(id);
        ResponseEntity<List<DepartmentDto>> response = departmentClient.findByOrganization(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            organizationDto.setDepartments(response.getBody());
        }
        return ResponseEntity.ok(organizationDto);
    }

    @GetMapping("{id}/with-departments-and-employees")
    public ResponseEntity<OrganizationDto> getByIdWithDepartmentsAndEmployees(@PathVariable Long id) {
        log.info("Organization get: id={} with departments and employees", id);
        var organizationDto = organizationService.findById(id);
        ResponseEntity<List<DepartmentDto>> response = departmentClient.findByOrganizationWithEmployees(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            organizationDto.setDepartments(response.getBody());
        }
        return ResponseEntity.ok(organizationDto);
    }

    @GetMapping("{id}/with-employees")
    public ResponseEntity<OrganizationDto> getByIdWithEmployees(@PathVariable Long id) {
        log.info("Organization find : id={} with employees", id);
        var organizationDto = organizationService.findById(id);
        ResponseEntity<List<EmployeeDto>> response = employeeClient.findByOrganization(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            organizationDto.setEmployees(response.getBody());
        }
        return ResponseEntity.ok(organizationDto);
    }

    @PostMapping
    public ResponseEntity<OrganizationDto> save(@RequestBody OrganizationDto organizationDto) {
        log.info("Organization save: {}", organizationDto);
        var dto = organizationService.save(organizationDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri()).body(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<OrganizationDto> update(@PathVariable Long id, @RequestBody @Validated OrganizationDto organizationDto) {
        log.info("Organization update id={}: {}", id, organizationDto);
        return ResponseEntity.accepted().body(organizationService.update(id, organizationDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("Organization delete: id={}", id);
        organizationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
