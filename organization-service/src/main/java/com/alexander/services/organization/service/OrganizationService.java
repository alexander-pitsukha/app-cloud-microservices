package com.alexander.services.organization.service;

import com.alexander.services.library.dto.AutocompleteData;
import com.alexander.services.library.dto.OrganizationDto;
import com.alexander.services.library.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrganizationService extends Service<OrganizationDto, Page<OrganizationDto>, List<AutocompleteData>, Long> {
}
