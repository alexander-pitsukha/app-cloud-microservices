package com.alexander.services.organization.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrganizationRequest {

    @NotNull
    @NotBlank
    private String name;

    private String address;

    private List<Long> departments = new ArrayList<>();

    private List<Long> employees = new ArrayList<>();

}
