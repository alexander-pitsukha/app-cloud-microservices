package com.alexander.services.department.model;

import com.alexander.domain.AbstractTimestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "departments")
public class Department extends AbstractTimestamp {

    @Column(name = "name")
    private String name;

    @Column(name = "organization_id")
    private Long organizationId;

}
