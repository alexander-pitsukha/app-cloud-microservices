package com.alexander.services.employee.model;

import com.alexander.domain.AbstractTimestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee extends AbstractTimestamp {

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "position")
    private String position;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "department_id")
    private Long departmentId;

}
