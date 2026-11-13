package com.alexander.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrganizationDto extends AbstractLongIdentifiableDto {

    @NotBlank
    private String name;

    private String address;

    private List<DepartmentDto> departments = new ArrayList<>();

    private List<EmployeeDto> employees = new ArrayList<>();

}
