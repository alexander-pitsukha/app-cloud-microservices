package com.alexander.services.organization.entity;

import com.alexander.services.library.domain.AbstractEntityAt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "organizations")
public class Organization extends AbstractEntityAt {

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

}
