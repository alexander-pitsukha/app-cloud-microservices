package com.alexander.services.identity.validator;

import com.alexander.services.identity.controller.request.UserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UserRequestValidator implements ConstraintValidator<PasswordMatches, UserRequest> {

    @Override
    public boolean isValid(UserRequest userRequest, ConstraintValidatorContext context) {
        return Objects.equals(userRequest.getPassword(), userRequest.getMatchingPassword());
    }

}
