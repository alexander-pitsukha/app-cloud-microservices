package com.alexander.services.identity.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest extends UpdatePasswordRequest {

    @NotBlank(message = "{validation.token.for.reset.not.blank}")
    private String token;

}
