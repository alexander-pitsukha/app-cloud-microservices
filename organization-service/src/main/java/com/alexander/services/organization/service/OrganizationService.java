package com.alexander.services.organization.service;

import com.alexander.dto.OrganizationDto;
import com.alexander.services.organization.controller.request.OrganizationRequest;

import java.util.List;

public interface OrganizationService {

    List<OrganizationDto> findAll(int page, int size);

    OrganizationDto findById(Long id);

    OrganizationDto add(OrganizationRequest organizationRequest);

    OrganizationDto update(Long id, OrganizationRequest organizationRequest);

    void delete(Long id);

}
