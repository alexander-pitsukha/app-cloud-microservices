package com.alexander.services.department.entity;

import com.alexander.services.library.domain.AbstractEntityAt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "departments")
public class Department extends AbstractEntityAt {

    @Column(name = "name")
    private String name;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

}
