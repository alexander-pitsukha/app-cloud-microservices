package com.alexander.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
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
