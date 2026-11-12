package com.alexander.services.employee.controller;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.EmployeeDto;
import com.alexander.services.employee.service.EmployeeService;
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
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Page<EmployeeDto>> getAll(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        log.info("Employee get");
        return ResponseEntity.ok(employeeService.findAll(page != null ? page : 0, size != null ? size : 0));
    }

    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getAutocomplete(@RequestParam String query, @RequestParam Integer limit) {
        log.info("Employee get (autocomplete).");
        return ResponseEntity.ok(employeeService.findAutocomplete(query, limit));
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
        log.info("Employee get: id={}", id);
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @GetMapping("department/{departmentId}")
    public ResponseEntity<List<EmployeeDto>> getByDepartment(@PathVariable Long departmentId) {
        log.info("Employee get: departmentId={}", departmentId);
        return ResponseEntity.ok(employeeService.findByDepartment(departmentId));
    }

    @GetMapping("organization/{organizationId}")
    public ResponseEntity<List<EmployeeDto>> getByOrganization(@PathVariable Long organizationId) {
        log.info("Employee get: organizationId={}", organizationId);
        return ResponseEntity.ok(employeeService.findByOrganization(organizationId));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> save(@RequestBody EmployeeDto employeeDto) {
        log.info("Employee save: {}", employeeDto);
        var dto = employeeService.save(employeeDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri()).body(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestBody @Validated EmployeeDto employeeDto) {
        log.info("Employee update id={}: {}", id, employeeDto);
        return ResponseEntity.accepted().body(employeeService.update(id, employeeDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("Employee delete: id={}", id);
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
