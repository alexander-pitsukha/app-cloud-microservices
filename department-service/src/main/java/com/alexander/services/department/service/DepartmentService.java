package com.alexander.services.department.service;

import com.alexander.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDto> findAll(int page, int size);

    DepartmentDto findById(Long id);

    List<DepartmentDto> findByOrganization(Long organizationId);

    List<DepartmentDto> findByOrganizationWithEmployees(Long organizationId);

    DepartmentDto add(DepartmentDto departmentDto);

    DepartmentDto update(Long id, DepartmentDto departmentDto);

    void delete(Long id);

}
