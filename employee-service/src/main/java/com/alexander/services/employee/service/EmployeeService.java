package com.alexander.services.employee.service;

import com.alexander.services.library.dto.AutocompleteData;
import com.alexander.services.library.dto.EmployeeDto;
import com.alexander.services.library.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService extends Service<EmployeeDto, Page<EmployeeDto>, List<AutocompleteData>, Long> {

    List<EmployeeDto> findByDepartment(Long departmentId);

    List<EmployeeDto> findByOrganization(Long organizationId);

}
