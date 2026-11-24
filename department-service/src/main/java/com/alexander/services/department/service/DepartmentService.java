package com.alexander.services.department.service;

import com.alexander.services.library.dto.AutocompleteData;
import com.alexander.services.library.dto.DepartmentDto;
import com.alexander.services.library.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService extends Service<DepartmentDto, Page<DepartmentDto>, List<AutocompleteData>, Long> {

    List<DepartmentDto> findByOrganization(Long organizationId);

    List<DepartmentDto> findByOrganizationWithEmployees(Long organizationId);

}
