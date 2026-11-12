package com.alexander.identityservice.model;

import com.alexander.domain.AbstractIdentifiable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends AbstractIdentifiable<Long> {

    @Column(name = "token", unique = true, nullable = false, length = 600)
    private String token;

    @Column(name = "expire_date", nullable = false)
    private LocalDateTime expireDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

}
