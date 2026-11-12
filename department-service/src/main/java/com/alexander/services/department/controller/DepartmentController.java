package com.alexander.services.department.controller;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.DepartmentDto;
import com.alexander.dto.EmployeeDto;
import com.alexander.services.department.client.EmployeeClient;
import com.alexander.services.department.service.DepartmentService;
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
public class DepartmentController {

    private final DepartmentService departmentService;
    private final EmployeeClient employeeClient;

    @GetMapping
    public ResponseEntity<Page<DepartmentDto>> getAll(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        log.info("Department get");
        return ResponseEntity.ok(departmentService.findAll(page != null ? page : 0, size != null ? size : 0));
    }

    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getAutocomplete(@RequestParam String query, @RequestParam Integer limit) {
        log.info("Department get (autocomplete)");
        return ResponseEntity.ok(departmentService.findAutocomplete(query, limit));
    }

    @GetMapping("{id}")
    public ResponseEntity<DepartmentDto> getById(@PathVariable Long id) {
        log.info("Department get: id={}", id);
        return ResponseEntity.ok(departmentService.findById(id));
    }

    @GetMapping("{id}/with-employees")
    public ResponseEntity<DepartmentDto> getByIdWithEmployees(@PathVariable Long id) {
        log.info("Department get: id={} with employees", id);
        var departmentDto = departmentService.findById(id);
        ResponseEntity<List<EmployeeDto>> response = employeeClient.getByDepartment(departmentDto.getId());
        if (response.getStatusCode().is2xxSuccessful()) {
            departmentDto.setEmployees(response.getBody());
        }
        return ResponseEntity.ok(departmentDto);
    }

    @GetMapping("organization/{organizationId}")
    public ResponseEntity<List<DepartmentDto>> getByOrganization(@PathVariable Long organizationId) {
        log.info("Department get: organizationId={}", organizationId);
        return ResponseEntity.ok(departmentService.findByOrganization(organizationId));
    }

    @GetMapping("organization/{organizationId}/with-employees")
    public ResponseEntity<List<DepartmentDto>> getByOrganizationWithEmployees(@PathVariable Long organizationId) {
        log.info("Department get: organizationId={} with employees", organizationId);
        List<DepartmentDto> departmentDtos = departmentService.findByOrganizationWithEmployees(organizationId);
        departmentDtos.forEach(dto -> {
            ResponseEntity<List<EmployeeDto>> response = employeeClient.getByDepartment(dto.getId());
            if (response.getStatusCode().is2xxSuccessful()) {
                dto.setEmployees(response.getBody());
            }
        });
        return ResponseEntity.ok(departmentDtos);
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> save(@RequestBody @Validated DepartmentDto departmentDto) {
        log.info("Department save: {}", departmentDto);
        var dto = departmentService.save(departmentDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri()).body(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<DepartmentDto> update(@PathVariable Long id, @RequestBody @Validated DepartmentDto departmentDto) {
        log.info("Department update id={}: {}", id, departmentDto);
        return ResponseEntity.accepted().body(departmentService.update(id, departmentDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("Department delete: id={}", id);
        departmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
