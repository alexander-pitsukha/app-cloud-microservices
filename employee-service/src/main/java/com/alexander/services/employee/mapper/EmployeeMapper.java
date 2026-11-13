package com.alexander.services.employee.mapper;

import com.alexander.dto.EmployeeDto;
import com.alexander.mapper.BaseMapper;
import com.alexander.services.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper extends BaseMapper<Employee, EmployeeDto> {

    @Mapping(target = "id", source = "employee.id")
    EmployeeDto toDto(Employee employee);

    Employee toEntity(EmployeeDto employeeDto);

    List<EmployeeDto> toDtos(List<Employee> employees);

}
