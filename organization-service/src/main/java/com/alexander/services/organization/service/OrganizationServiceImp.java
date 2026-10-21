package com.alexander.services.organization.service;

import com.alexander.dto.OrganizationDto;
import com.alexander.services.organization.controller.request.OrganizationRequest;
import com.alexander.services.organization.mapper.OrganizationMapper;
import com.alexander.services.organization.model.Organization;
import com.alexander.services.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrganizationServiceImp implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public List<OrganizationDto> findAll(int page, int size) {
        Stream<Organization> stream;
        if (page == 0 && size == 0) {
            List<Organization> organizations = organizationRepository.findAll();
            stream = organizations.stream();
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<Organization> organizations = organizationRepository.findAll(pageable);
            stream = organizations.stream();
        }
        return stream.map(organizationMapper::toDto).toList();
    }

    @Override
    public OrganizationDto findById(Long id) {
        var organization = organizationRepository.findById(id).orElseThrow();
        return organizationMapper.toDto(organization);
    }

    @Transactional
    @Override
    public OrganizationDto add(OrganizationRequest organizationRequest) {
        var organization = organizationMapper.toEntity(organizationRequest);
        organization = organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }

    @Transactional
    @Override
    public OrganizationDto update(Long id, OrganizationRequest organizationRequest) {
        if (!organizationRepository.existsById(id)) {
            throw new IllegalArgumentException();
        }
        var organization = organizationRepository.getById(id);
        organization.setName(organizationRequest.getName());
        organization.setAddress(organizationRequest.getAddress());
        organization.setDepartmentIds(organizationRequest.getDepartments());
        organization.setEmployeeIds(organizationRequest.getEmployees());
        return organizationMapper.toDto(organization);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new IllegalArgumentException();
        }
        organizationRepository.deleteById(id);
    }

}
