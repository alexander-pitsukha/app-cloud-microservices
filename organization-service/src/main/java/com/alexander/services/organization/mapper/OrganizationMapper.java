package com.alexander.services.organization.mapper;

import com.alexander.dto.OrganizationDto;
import com.alexander.services.organization.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {

    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    @Mapping(target = "id", source = "organization.id")
    OrganizationDto toDto(Organization organization);

    Organization toEntity(OrganizationDto organizationDto);

}
