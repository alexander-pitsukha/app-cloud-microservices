package com.alexander.services.department.model;

import com.alexander.converter.LongListConverter;
import com.alexander.domain.AbstractTimestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "departments")
public class Department extends AbstractTimestamp {

    @Column(name = "name")
    private String name;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "employee_ids")
    @Convert(converter = LongListConverter.class)
    private List<Long> employeeIds = new ArrayList<>();

}
