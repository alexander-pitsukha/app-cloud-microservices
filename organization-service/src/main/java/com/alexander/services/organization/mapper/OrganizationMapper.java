package com.alexander.services.organization.mapper;

import com.alexander.services.library.dto.OrganizationDto;
import com.alexander.services.library.mapper.BaseMapper;
import com.alexander.services.organization.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper extends BaseMapper<Organization, OrganizationDto> {

    @Mapping(target = "id", source = "organization.id")
    OrganizationDto toDto(Organization organization);

    Organization toEntity(OrganizationDto organizationDto);

}
