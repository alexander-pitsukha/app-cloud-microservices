package com.alexander.services.department.service;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.DepartmentDto;
import com.alexander.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService extends Service<DepartmentDto, Page<DepartmentDto>, List<AutocompleteData>, Long> {

    List<DepartmentDto> findByOrganization(Long organizationId);

    List<DepartmentDto> findByOrganizationWithEmployees(Long organizationId);

}
