package com.alexander.services.identity.entity;

import com.alexander.services.library.domain.AbstractIdentifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends AbstractIdentifiable<Long> {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();

}
