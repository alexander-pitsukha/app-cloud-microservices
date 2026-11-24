package com.alexander.services.identity.controller.request;

import com.alexander.services.identity.validator.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class UserRequest {

    private String username;

    @Email
    private String email;

    @Size(min = 6, message = "{validation.size.password}")
    private String password;

    @JsonProperty("matching_password")
    @NotBlank
    private String matchingPassword;

    @NotNull
    private Long roleId;

}
