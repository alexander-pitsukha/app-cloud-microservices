package com.alexander.services.employee.entity;

import com.alexander.services.library.domain.AbstractEntityAt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee extends AbstractEntityAt {

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "position")
    private String position;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

}
