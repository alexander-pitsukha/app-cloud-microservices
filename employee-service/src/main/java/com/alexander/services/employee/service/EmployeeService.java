package com.alexander.services.employee.service;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.EmployeeDto;
import com.alexander.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService extends Service<EmployeeDto, Page<EmployeeDto>, List<AutocompleteData>, Long> {

    List<EmployeeDto> findByDepartment(Long departmentId);

    List<EmployeeDto> findByOrganization(Long organizationId);

}
