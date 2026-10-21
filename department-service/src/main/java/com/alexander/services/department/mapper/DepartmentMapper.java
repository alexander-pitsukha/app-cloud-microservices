package com.alexander.services.department.mapper;

import com.alexander.dto.DepartmentDto;
import com.alexander.services.department.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @Mapping(target = "id", source = "department.id")
    DepartmentDto toDto(Department department);

    Department toEntity(DepartmentDto departmentDto);

    List<DepartmentDto> toDtos(List<Department> departments);

}
