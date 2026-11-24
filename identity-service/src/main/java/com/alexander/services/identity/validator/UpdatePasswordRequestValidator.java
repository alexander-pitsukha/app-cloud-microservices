package com.alexander.services.identity.validator;

import com.alexander.services.identity.controller.request.UpdatePasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UpdatePasswordRequestValidator implements ConstraintValidator<PasswordMatches, UpdatePasswordRequest> {

    @Override
    public boolean isValid(UpdatePasswordRequest request, ConstraintValidatorContext context) {
        return Objects.equals(request.getNewPassword(), request.getMatchingNewPassword());
    }

}
