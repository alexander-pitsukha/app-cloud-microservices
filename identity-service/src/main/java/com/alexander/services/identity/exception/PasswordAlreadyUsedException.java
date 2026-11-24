package com.alexander.services.identity.exception;

public class PasswordAlreadyUsedException extends RuntimeException {

    public PasswordAlreadyUsedException(String message) {
        super(message);
    }

}
