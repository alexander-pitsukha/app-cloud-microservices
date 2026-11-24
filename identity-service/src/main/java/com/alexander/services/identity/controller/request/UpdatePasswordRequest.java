package com.alexander.services.identity.controller.request;

import com.alexander.services.identity.validator.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class UpdatePasswordRequest {

    @JsonProperty("current_password")
    @NotBlank(message = "{validation.password.current.not.blank}")
    private String currentPassword;

    @JsonProperty("new_password")
    @Size(min = 6, message = "{validation.size.password}")
    private String newPassword;

    @JsonProperty("matching_new_password")
    @NotBlank
    private String matchingNewPassword;

}
