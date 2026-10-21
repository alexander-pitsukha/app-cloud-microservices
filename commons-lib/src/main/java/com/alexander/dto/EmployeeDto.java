package com.alexander.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto extends AbstractLongIdentifiableDto {

    @NotNull
    @NotBlank
    private String name;

    private int age;

    @Email
    private String email;

    private String position;

    private Long organizationId;

    private Long departmentId;

}
