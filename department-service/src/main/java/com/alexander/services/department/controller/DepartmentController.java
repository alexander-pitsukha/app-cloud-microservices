package com.alexander.services.department.controller;

import com.alexander.dto.DepartmentDto;
import com.alexander.dto.EmployeeDto;
import com.alexander.services.department.client.EmployeeClient;
import com.alexander.services.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<List<DepartmentDto>> findAll(@RequestParam int page, @RequestParam int size) {
        log.info("Department find");
        return ResponseEntity.ok(departmentService.findAll(page, size));
    }

    @GetMapping("{id}")
    public ResponseEntity<DepartmentDto> findById(@PathVariable("id") Long id) {
        log.info("Department find: id={}", id);
        return ResponseEntity.ok(departmentService.findById(id));
    }

    @GetMapping("organization/{organizationId}")
    public ResponseEntity<List<DepartmentDto>> findByOrganization(@PathVariable("organizationId") Long organizationId) {
        log.info("Department find: organizationId={}", organizationId);
        return ResponseEntity.ok(departmentService.findByOrganization(organizationId));
    }

    @GetMapping("organization/{organizationId}/with-employees")
    public ResponseEntity<List<DepartmentDto>> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId) {
        log.info("Department find: organizationId={}", organizationId);
        List<DepartmentDto> departmentDtos = departmentService.findByOrganizationWithEmployees(organizationId);
        departmentDtos.forEach(dto -> {
            ResponseEntity<List<EmployeeDto>> response = employeeClient.findByDepartment(dto.getId());
            if (response.getStatusCode().is2xxSuccessful()) {
                dto.setEmployees(response.getBody());
            }
        });
        return ResponseEntity.ok(departmentDtos);
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> add(@RequestBody @Validated DepartmentDto departmentDto) {
        log.info("Department add: {}", departmentDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(departmentDto.getId()).toUri()).body(departmentService.add(departmentDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<DepartmentDto> update(@PathVariable Long id, @RequestBody @Validated DepartmentDto departmentDto) {
        log.info("Department update id={}: {}", id, departmentDto);
        return ResponseEntity.accepted().body(departmentService.update(id, departmentDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("Department delete: id={}", id);
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
