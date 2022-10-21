package com.alexander.services.organization.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Department {

    private Long id;
    private String name;
    private List<Employee> employees = new ArrayList<>();

}
