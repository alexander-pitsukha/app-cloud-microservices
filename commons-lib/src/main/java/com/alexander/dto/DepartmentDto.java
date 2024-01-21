package com.alexander.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DepartmentDto extends AbstractLongIdentifiableDto {

    @NotNull
    @NotBlank
    private String name;

    private Long organizationId;

    private List<EmployeeDto> employees = new ArrayList<>();

}
