package com.alexander.services.organization.service;

import com.alexander.dto.OrganizationDto;

import java.util.List;

public interface OrganizationService {

    List<OrganizationDto> findAll(int page, int size);

    OrganizationDto findById(Long id);

    OrganizationDto add(OrganizationDto organizationDto);

    OrganizationDto update(Long id, OrganizationDto organizationDto);

    void delete(Long id);

}
