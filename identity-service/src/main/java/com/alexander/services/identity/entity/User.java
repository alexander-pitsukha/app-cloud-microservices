package com.alexander.services.identity.entity;

import com.alexander.services.library.domain.AbstractEntityAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User extends AbstractEntityAt {

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "last_password_id")
    private Password password;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified;

    @Column(name = "email_verification_token", unique = true)
    private String emailVerificationToken;

    @Column(name = "email_verification_token_expires_at")
    private LocalDateTime emailVerificationTokenExpiresAt;

    @Column(name = "password_reset_token", unique = true)
    private String passwordResetToken;

    @Column(name = "password_reset_token_expires_at")
    private LocalDateTime passwordResetTokenExpiresAt;

    @OneToMany(mappedBy = "user")
    @OrderBy("createDate DESC")
    private List<Password> passwords = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
