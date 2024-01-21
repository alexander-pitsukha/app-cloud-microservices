package com.alexander.services.organization.mapper;

import com.alexander.dto.DepartmentDto;
import com.alexander.dto.EmployeeDto;
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
    @Mapping(target = "departments", source = "departmentIds")
    @Mapping(target = "employees", source = "employeeIds")
    OrganizationDto toDto(Organization organization);

    @Mapping(target = "departmentIds", source = "departments")
    @Mapping(target = "employeeIds", source = "employees")
    Organization toEntity(OrganizationDto organizationDto);

    default Long toDepartmentId(DepartmentDto dto) {
        return dto == null ? null : dto.getId();
    }

    default Long toEmployeeId(EmployeeDto dto) {
        return dto == null ? null : dto.getId();
    }

    default DepartmentDto toDepartment(Long id) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(id);
        return departmentDto;
    }

    default EmployeeDto toEmployee(Long id) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(id);
        return employeeDto;
    }

}
