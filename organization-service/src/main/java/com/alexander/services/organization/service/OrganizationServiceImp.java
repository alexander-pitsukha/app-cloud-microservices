package com.alexander.services.organization.service;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.OrganizationDto;
import com.alexander.services.organization.mapper.OrganizationMapper;
import com.alexander.services.organization.model.Organization;
import com.alexander.services.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrganizationServiceImp implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public Page<OrganizationDto> findAll(int page, int size) {
        Page<Organization> organizations;
        if (page == 0 && size == 0) {
            organizations = organizationRepository.findAll(Pageable.unpaged());
        } else {
            organizations = organizationRepository.findAll(PageRequest.of(page, size));
        }
        return organizations.map(organizationMapper::toDto);
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

    @Override
    public OrganizationDto findById(Long id) {
        var organization = organizationRepository.findById(id).orElseThrow();
        return organizationMapper.toDto(organization);
    }

    @Transactional
    @Override
    public OrganizationDto save(OrganizationDto organizationDto) {
        var organization = organizationMapper.toEntity(organizationDto);
        organization = organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }

    @Transactional
    @Override
    public OrganizationDto update(Long id, OrganizationDto organizationDto) {
        Organization organization = organizationRepository.findById(id).orElseThrow();
        organization.setName(organizationDto.getName());
        organization.setAddress(organizationDto.getAddress());
        return organizationMapper.toDto(organization);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        organizationRepository.deleteById(id);
    }

}
