package com.alexander.services.organization.model;

import com.alexander.domain.AbstractTimestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "organizations")
public class Organization extends AbstractTimestamp {

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

}
