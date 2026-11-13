package com.alexander.services.department.mapper;

import com.alexander.dto.DepartmentDto;
import com.alexander.mapper.BaseMapper;
import com.alexander.services.department.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper extends BaseMapper<Department, DepartmentDto> {

    @Mapping(target = "id", source = "department.id")
    DepartmentDto toDto(Department department);

    Department toEntity(DepartmentDto departmentDto);

    List<DepartmentDto> toDtos(List<Department> departments);

}
