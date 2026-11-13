package com.alexander.services.organization.service;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.OrganizationDto;
import com.alexander.service.impl.ServiceImpl;
import com.alexander.services.organization.entity.Organization;
import com.alexander.services.organization.mapper.OrganizationMapper;
import com.alexander.services.organization.repository.OrganizationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrganizationServiceImp extends ServiceImpl<OrganizationRepository, OrganizationMapper, Organization, OrganizationDto> implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImp(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        super(organizationRepository, organizationMapper);
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public List<AutocompleteData> findAutocomplete(String query, Integer limit) {
        final String fieldName = "name";
        Page<Organization> organizations;
        if (!ObjectUtils.isEmpty(query)) {
            organizations = organizationRepository.findAllByNameLikeAsc(query, PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        } else {
            organizations = organizationRepository.findAll(PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        }
        return organizations.map(organization -> new AutocompleteData(organization.getId(), organization.getName())).toList();
    }

    @Transactional
    @Override
    public OrganizationDto update(Long id, OrganizationDto organizationDto) {
        Organization organization = organizationRepository.findById(id).orElseThrow();
        organization.setName(organizationDto.getName());
        organization.setAddress(organizationDto.getAddress());
        return organizationMapper.toDto(organization);
    }

}
