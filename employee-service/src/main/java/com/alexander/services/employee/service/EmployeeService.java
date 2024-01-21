package com.alexander.services.employee.service;

import com.alexander.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDto> findAll(int page, int size);

    EmployeeDto findById(Long id);

    List<EmployeeDto> findByDepartment(Long departmentId);

    List<EmployeeDto> findByOrganization(Long organizationId);

    EmployeeDto add(EmployeeDto employeeDto);

    EmployeeDto update(Long id, EmployeeDto employeeDto);

    void delete(Long id);

}
