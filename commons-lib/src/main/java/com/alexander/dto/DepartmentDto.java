package com.alexander.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DepartmentDto extends AbstractLongIdentifiableDto {

    @NotBlank
    private String name;

    @NotNull
    private Long organizationId;

    private List<EmployeeDto> employees = new ArrayList<>();

}
