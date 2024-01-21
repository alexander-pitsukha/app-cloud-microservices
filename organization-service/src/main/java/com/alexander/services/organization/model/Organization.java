package com.alexander.services.organization.model;

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
@Table(name = "organizations")
public class Organization extends AbstractTimestamp {

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "department_ids")
    @Convert(converter = LongListConverter.class)
    private List<Long> departmentIds = new ArrayList<>();

    @Column(name = "employee_ids")
    @Convert(converter = LongListConverter.class)
    private List<Long> employeeIds = new ArrayList<>();

}
