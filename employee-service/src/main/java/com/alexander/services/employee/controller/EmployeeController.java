package com.alexander.services.employee.controller;

import com.alexander.dto.EmployeeDto;
import com.alexander.services.employee.service.EmployeeService;
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
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> findAll(@RequestParam int page, @RequestParam int size) {
        log.info("Employee find");
        return ResponseEntity.ok(employeeService.findAll(page, size));
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> findById(@PathVariable("id") Long id) {
        log.info("Employee find: id={}", id);
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @GetMapping("department/{departmentId}")
    public ResponseEntity<List<EmployeeDto>> findByDepartment(@PathVariable("departmentId") Long departmentId) {
        log.info("Employee find: departmentId={}", departmentId);
        return ResponseEntity.ok(employeeService.findByDepartment(departmentId));
    }

    @GetMapping("organization/{organizationId}")
    public ResponseEntity<List<EmployeeDto>> findByOrganization(@PathVariable("organizationId") Long organizationId) {
        log.info("Employee find: organizationId={}", organizationId);
        return ResponseEntity.ok(employeeService.findByOrganization(organizationId));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> add(@RequestBody EmployeeDto employeeDto) {
        log.info("Employee add: {}", employeeDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employeeDto.getId()).toUri()).body(employeeService.add(employeeDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestBody @Validated EmployeeDto employeeDto) {
        log.info("Employee update id={}: {}", id, employeeDto);
        return ResponseEntity.accepted().body(employeeService.update(id, employeeDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("Employee delete: id={}", id);
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
