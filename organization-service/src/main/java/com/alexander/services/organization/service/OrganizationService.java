package com.alexander.services.organization.service;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.OrganizationDto;
import com.alexander.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrganizationService extends Service<OrganizationDto, Page<OrganizationDto>, List<AutocompleteData>, Long> {
}
