package com.alexander.services.identity.entity;

import com.alexander.services.library.domain.AbstractEntityAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "audit_login")
public class AuditLogin extends AbstractEntityAt {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

}
